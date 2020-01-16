package utils.http;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import utils.IOUtils;
import utils.http.callback.BaseHttpCallback;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CodingErrorAction;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public final class AsyncHttp {
    // 默认编码 UTF-8
    private static final String DEFAULT_ENCODE = "UTF-8";
    // 默认连接超时时间 15s
    private static final int DEFAULT_CONNECT_TIMEOUT = 15000;
    // 请求数据超时时间 6s
    private static final int DEFAULT_SOCKET_TIMEOUT = 6000;

    private String encode = DEFAULT_ENCODE;
    private int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    private CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.createDefault();

    public AsyncHttp() {
        this.httpAsyncClient = HttpAsyncClients.custom()
            .setConnectionManager(getConnectionManager()) // 设置连接池管理
            .setKeepAliveStrategy(getConnectionAliveStrategy()) // 设置连接存活策略
                .setDefaultRequestConfig(getRequestConfig())
        .build();
        this.httpAsyncClient.start();
    }


    public static Registry getRegistry(){
        Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
        try {
            SSLContext sslContext = SSLContext.getDefault();
            //创建自定义连接套接字工厂的注册表以供支持协议方案。
            socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext))
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return socketFactoryRegistry;
    }

    /**
     * 获取连接池管理
     * @return
     */
    public static PoolingNHttpClientConnectionManager getConnectionManager(){
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors()) // 线程数为CPU核心数
                .setConnectTimeout(30000) // 连接超时时间
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
                .setSocketTimeout(3000) // socket连接超时
                .setConnectTimeout(3000)// 连接超时
                .setConnectionRequestTimeout(5000) // 请求连接超时
                .build();
    }

    public ConnectionKeepAliveStrategy getConnectionAliveStrategy(){
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

    public void doGet(String url, BaseHttpCallback callback){
        HttpGet httpGet = (HttpGet) HttpMethod.getRequest(url,HttpMethod.GET);
        httpAsyncClient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                int state = result.getStatusLine().getStatusCode();
                if(state >= 200  && state < 300){
                    try {
                        callback.onSuccess(result.getEntity().getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    callback.onError(state,new NetWorkException());
                }
            }

            @Override
            public void failed(Exception ex) {
                callback.onFailure(ex);
            }

            @Override
            public void cancelled() {
                callback.onCancel();
            }
        });
    }






    public boolean shutdown(){
        if(httpAsyncClient == null){
            return false;
        }
        try {
            httpAsyncClient.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        new AsyncHttp().doGet("https://kmg343.gitbooks.io/httpcl-ient4-4-no2/content/231_guan_li_lian_jie_he_lian_jie_guan_li_qi.html", new BaseHttpCallback() {
            @Override
            public void onSuccess(InputStream inputStream) {
                String s = IOUtils.inputStream2String(inputStream, "UTF-8");
                System.out.println(s);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e);
            }

            @Override
            public void onError(int errorCode, Exception e) {
                System.out.println("errorCode = "+ errorCode + "-"+ e);
            }

            @Override
            public void onCancel() {
                System.out.println("oncanel");
            }
        });
    }
}
