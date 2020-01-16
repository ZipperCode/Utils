/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package httpclient.core;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.ConnectionClosedException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;

/**
 *
 * 同步反向代理
 * Elemental HTTP/1.1 reverse proxy.
 */
public class ElementalReverseProxy {

    private static final String HTTP_IN_CONN = "http.proxy.in-conn";
    private static final String HTTP_OUT_CONN = "http.proxy.out-conn";
    private static final String HTTP_CONN_KEEPALIVE = "http.proxy.conn-keepalive";

    public static void main(final String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: <hostname[:port]> [listener port]");
            System.exit(1);
        }
        final HttpHost targetHost = HttpHost.create(args[0]);
        int port = 8080;
        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        System.out.println("Reverse proxy to " + targetHost);

        final Thread t = new RequestListenerThread(port, targetHost);
        t.setDaemon(false);
        t.start();
    }

    static class ProxyHandler implements HttpRequestHandler  {

        private final HttpHost target;
        private final HttpProcessor httpproc;
        private final HttpRequestExecutor httpexecutor;
        private final ConnectionReuseStrategy connStrategy;

        public ProxyHandler(
                final HttpHost target,
                final HttpProcessor httpproc,
                final HttpRequestExecutor httpexecutor) {
            super();
            this.target = target;
            this.httpproc = httpproc;
            this.httpexecutor = httpexecutor;
            this.connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
        }

        public void handle(
                final HttpRequest request,
                final HttpResponse response,
                final HttpContext context) throws HttpException, IOException {

            final DefaultBHttpClientConnection conn = (DefaultBHttpClientConnection) context.getAttribute(
                    HTTP_OUT_CONN);

            if (!conn.isOpen() || conn.isStale()) {
                final Socket outsocket = new Socket(this.target.getHostName(), this.target.getPort() >= 0 ? this.target.getPort() : 80);
                conn.bind(outsocket);
                System.out.println("Outgoing connection to " + outsocket.getInetAddress());
            }

            context.setAttribute(HttpCoreContext.HTTP_CONNECTION, conn);
            context.setAttribute(HttpCoreContext.HTTP_TARGET_HOST, this.target);

            System.out.println(">> Request URI: " + request.getRequestLine().getUri());

            // Remove hop-by-hop headers
            request.removeHeaders(HTTP.TARGET_HOST);
            request.removeHeaders(HTTP.CONTENT_LEN);
            request.removeHeaders(HTTP.TRANSFER_ENCODING);
            request.removeHeaders(HTTP.CONN_DIRECTIVE);
            request.removeHeaders("Keep-Alive");
            request.removeHeaders("Proxy-Authenticate");
            request.removeHeaders("TE");
            request.removeHeaders("Trailers");
            request.removeHeaders("Upgrade");

            this.httpexecutor.preProcess(request, this.httpproc, context);
            final HttpResponse targetResponse = this.httpexecutor.execute(request, conn, context);
            this.httpexecutor.postProcess(response, this.httpproc, context);

            // Remove hop-by-hop headers
            targetResponse.removeHeaders(HTTP.CONTENT_LEN);
            targetResponse.removeHeaders(HTTP.TRANSFER_ENCODING);
            targetResponse.removeHeaders(HTTP.CONN_DIRECTIVE);
            targetResponse.removeHeaders("Keep-Alive");
            targetResponse.removeHeaders("TE");
            targetResponse.removeHeaders("Trailers");
            targetResponse.removeHeaders("Upgrade");

            response.setStatusLine(targetResponse.getStatusLine());
            response.setHeaders(targetResponse.getAllHeaders());
            response.setEntity(targetResponse.getEntity());

            System.out.println("<< Response: " + response.getStatusLine());

            final boolean keepalive = this.connStrategy.keepAlive(response, context);
            context.setAttribute(HTTP_CONN_KEEPALIVE, new Boolean(keepalive));
        }

    }

    static class RequestListenerThread extends Thread {
        // 目标主机
        private final HttpHost target;
        // 服务端Socket
        private final ServerSocket serversocket;
        // Http 服务
        private final HttpService httpService;

        public RequestListenerThread(final int port, final HttpHost target) throws IOException {
            this.target = target;
            this.serversocket = new ServerSocket(port);

            // 为传入连接设置HTTP协议处理器 参数为响应拦截器
            final HttpProcessor inhttpproc = new ImmutableHttpProcessor(
                    new ResponseDate(), // 将日期加入响应头中
                    new ResponseServer("Test/1.1"), // 响应添加服务
                    new ResponseContent(),// 响应实体
                    new ResponseConnControl()); // 将连接主机添加到响应中

            // 为传出连接设置HTTP协议处理器
            final HttpProcessor outhttpproc = new ImmutableHttpProcessor(
                    new RequestContent(), // 添加响应内容
                    new RequestTargetHost(), // 添加目标主机
                    new RequestConnControl(), // 添加连接主机
                    new RequestUserAgent("Test/1.1"), // 添加用户头
                    new RequestExpectContinue(true)); //通过添加expect头来启用“expect continue”握手 用于客户端协议处理器

            // 设置传出请求执行器
            final HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

            // 设置传入请求处理程序
            final UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
            reqistry.register("*", new ProxyHandler(
                    this.target,
                    outhttpproc,
                    httpexecutor));

            // Set up the HTTP service
            this.httpService = new HttpService(inhttpproc, reqistry);
        }

        @Override
        public void run() {
            System.out.println("Listening on port " + this.serversocket.getLocalPort());
            while (!Thread.interrupted()) {
                try {
                    final int bufsize = 8 * 1024;
                    // 设置传入的HTTP连接，服务端接受的客户端连接
                    final Socket insocket = this.serversocket.accept();
                    final DefaultBHttpServerConnection inconn = new DefaultBHttpServerConnection(bufsize);
                    System.out.println("Incoming connection from " + insocket.getInetAddress());
                    inconn.bind(insocket);

                    // Set up outgoing HTTP connection
                    final DefaultBHttpClientConnection outconn = new DefaultBHttpClientConnection(bufsize);

                    // Start worker thread
                    final Thread t = new ProxyThread(this.httpService, inconn, outconn);
                    t.setDaemon(true);
                    t.start();
                } catch (final InterruptedIOException ex) {
                    break;
                } catch (final IOException e) {
                    System.err.println("I/O error initialising connection thread: "
                            + e.getMessage());
                    break;
                }
            }
        }
    }

    static class ProxyThread extends Thread {

        private final HttpService httpservice;
        private final DefaultBHttpServerConnection inconn;
        private final DefaultBHttpClientConnection outconn;

        public ProxyThread(
                final HttpService httpservice,
                final DefaultBHttpServerConnection inconn,
                final DefaultBHttpClientConnection outconn) {
            super();
            this.httpservice = httpservice;
            this.inconn = inconn;
            this.outconn = outconn;
        }

        @Override
        public void run() {
            System.out.println("New connection thread");
            final HttpContext context = new BasicHttpContext(null);

            // Bind connection objects to the execution context
            context.setAttribute(HTTP_IN_CONN, this.inconn);
            context.setAttribute(HTTP_OUT_CONN, this.outconn);

            try {
                while (!Thread.interrupted()) {
                    if (!this.inconn.isOpen()) {
                        this.outconn.close();
                        break;
                    }

                    this.httpservice.handleRequest(this.inconn, context);

                    final Boolean keepalive = (Boolean) context.getAttribute(HTTP_CONN_KEEPALIVE);
                    if (!Boolean.TRUE.equals(keepalive)) {
                        this.outconn.close();
                        this.inconn.close();
                        break;
                    }
                }
            } catch (final ConnectionClosedException ex) {
                System.err.println("Client closed connection");
            } catch (final IOException ex) {
                System.err.println("I/O error: " + ex.getMessage());
            } catch (final HttpException ex) {
                System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
            } finally {
                try {
                    this.inconn.shutdown();
                } catch (final IOException ignore) {}
                try {
                    this.outconn.shutdown();
                } catch (final IOException ignore) {}
            }
        }

    }

}