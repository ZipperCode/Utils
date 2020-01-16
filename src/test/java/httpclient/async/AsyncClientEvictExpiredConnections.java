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
package httpclient.async;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.nio.reactor.ConnectingIOReactor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 连接驱逐
 * 本示例演示如何从连接池中退出过期和空闲的连接
 * Example demonstrating how to evict expired and idle connections
 * from the connection pool.
 */
public class AsyncClientEvictExpiredConnections {

    public static void main(String[] args) throws Exception {
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        // 创建连接池
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setMaxTotal(100); // 设置连接池最大数量
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setConnectionManager(cm)
                .build();
        try {
            httpclient.start();

            // create an array of URIs to perform GETs on
            String[] urisToGet = {
                "http://hc.apache.org/",
                "http://hc.apache.org/httpcomponents-core-ga/",
                "http://hc.apache.org/httpcomponents-client-ga/",
            };

            IdleConnectionEvictor connEvictor = new IdleConnectionEvictor(cm);
            connEvictor.start();
            // 多线程计数器，cout ！= 0 才会有任务进行
            final CountDownLatch latch = new CountDownLatch(urisToGet.length);
            for (final String uri: urisToGet) {
                final HttpGet httpget = new HttpGet(uri);
                httpclient.execute(httpget, new FutureCallback<HttpResponse>() {

                    @Override
                    public void completed(final HttpResponse response) {
                        latch.countDown(); // 计数器值减一
                        System.out.println(httpget.getRequestLine() + "->" + response.getStatusLine());
                    }

                    @Override
                    public void failed(final Exception ex) {
                        latch.countDown();
                        System.out.println(httpget.getRequestLine() + "->" + ex);
                    }

                    @Override
                    public void cancelled() {
                        latch.countDown();
                        System.out.println(httpget.getRequestLine() + " cancelled");
                    }

                });
            }
            latch.await(); // 如果计数器count = 0，则等待

            // 休眠10秒，让连接退出器执行其任务
            Thread.sleep(20000);

            // 关闭逐出器线程
            connEvictor.shutdown();
            connEvictor.join();

        } finally {
            httpclient.close();
        }
    }

    public static class IdleConnectionEvictor extends Thread {

        private final NHttpClientConnectionManager connMgr;

        private volatile boolean shutdown;

        public IdleConnectionEvictor(NHttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // 关闭过期的连接
                        connMgr.closeExpiredConnections();
                        // （可选）紧密连接
                        // 空闲时间超过5秒
                        connMgr.closeIdleConnections(5, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }

    }

}