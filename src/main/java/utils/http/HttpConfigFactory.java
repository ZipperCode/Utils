package utils.http;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import utils.http.persistent.PersistentCookieStore;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpConfigFactory {

    // 默认编码 UTF-8
    public static final String DEFAULT_ENCODE = "UTF-8";
    // 默认连接超时时间 15s
    public static final int DEFAULT_CONNECT_TIMEOUT = 20000;
    // 请求数据超时时间 6s
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;

    /**
     * 配置连接策略
     * @return
     */
    public static Registry getRegistry(){
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getDefault();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(sslContext, new DefaultHostnameVerifier()))
                .build();
        return sessionStrategyRegistry;
    }


    public static SSLIOSessionStrategy getSSLIOSessionStrategy() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(new TrustSelfSignedStrategy())
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }
        return new SSLIOSessionStrategy(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    /**
     * 获取连接池管理
     * @return
     */
    public static PoolingNHttpClientConnectionManager getConnectionManager(){
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors()) // 线程数为CPU核心数
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT) // 连接超时时间
                .setSoTimeout(30000) //
                .build();
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        // 创建连接池
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
        // 创建消息约束
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200) // 最大消息后
//                .setMaxLineLength(2000)
                .build();
        // 创建连接配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8) // 编码使用UTF-8
                .setMessageConstraints(messageConstraints)
                .build();
        // 将连接管理器配置为使用连接配置，默认情况下或针对特定主机。
        cm.setDefaultConnectionConfig(connectionConfig);
        // 设置连接池最大数量
        cm.setMaxTotal(200);
        // 设置最大连接路由
        cm.setDefaultMaxPerRoute(50);
        return cm;
    }

    public static RequestConfig getRequestConfig(){
        return RequestConfig.custom()
                // socket连接超时
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                // 连接超时
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                // 请求连接超时
                .setConnectionRequestTimeout(15000)
                // cookie策略设置为默认H
                .setCookieSpec(CookieSpecs.DEFAULT)
                // 设置允许多次重定向
                .setCircularRedirectsAllowed(true)
                .build();
    }

    public static ConnectionKeepAliveStrategy getConnectionAliveStrategy(){
        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = (HttpResponse response, HttpContext context)->{
            HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        return Long.parseLong(value) * 1000;
                    } catch(NumberFormatException ignore) {
                    }
                }
            }
            return 60 * 1000;
        };
        return connectionKeepAliveStrategy;
    }

    public static HttpRequestRetryHandler getHttpRequestRetryHandler(){
        return (IOException exception, int executionCount, HttpContext context)->{
            if(exception == null){ // 没有出现异常取消
                return false;
            }
            if(executionCount >= 3){ // 重试次数大于3次取消
                return false;
            }
            if(exception instanceof NoHttpResponseException){
                return true;
            }
            return false; //   其他情况取消
        };
    }

    public static CookieStore getCookieStore(){
        CookieStore cookieStore = new PersistentCookieStore("D:\\111\\a.txt");
        return cookieStore;
    }

    public static HttpContext getHttpContext(){
        CookieStore cookieStore = new PersistentCookieStore("D:\\111\\a.txt");
        HttpClientContext localHttpContext = HttpClientContext.create();
        localHttpContext.setCookieStore(cookieStore);
        return localHttpContext;
    }

}
