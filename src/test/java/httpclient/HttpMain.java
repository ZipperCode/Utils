package httpclient;

import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;

/**
 * @author 90604
 * @project utils
 * @date 2020/1/14
 **/
public class HttpMain {
    public static void test01() throws Exception {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = closeableHttpClient.execute(get);
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.baidu.com")
                .setPath("/search")
                .setParameter("q","httpclient")
                .build();

        // 消费实体
        HttpEntity entity;
        // 文件实体
        FileEntity fileEntity;
        // html表单实体
        UrlEncodedFormEntity urlEncodedFormEntity;
        HttpContext context;
        // 拦截器
        HttpRequestInterceptor httpRequestInterceptor;
        // 重试处理程序
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, org.apache.http.protocol.HttpContext context) {
                return false;
            }
        };
        //重定向处理
        LaxRedirectStrategy laxRedirectStrategy;
        // 连接管理器
        HttpClientConnectionManager httpClientConnectionManager;
        // 连接池管理
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
        // 长连接
        ConnectionKeepAliveStrategy connectionKeepAliveStrategy;

        HeaderElementIterator headerElementIterator;
        // 连接套接字工厂
        PlainConnectionSocketFactory plainConnectionSocketFactory;
        // 安全的连接套接字工厂
        LayeredConnectionSocketFactory layeredConnectionSocketFactory;
        //SSL/TLS
        KeyStore keyStore = KeyStore.getInstance("");
        SSLContext sslContext = SSLContexts.custom()
                .build();
        SSLConnectionSocketFactory sslConnectionSocketFactory;





    }


    public static void testSSL01() throws Exception {
        char[] passPhrase = "passphrase".toCharArray();
        // 初始化秘钥和信任材料
        KeyStore ksKeys = KeyStore.getInstance("JKS");
        ksKeys.load(new FileInputStream("testKeys"),passPhrase);
        KeyStore ksTrust = KeyStore.getInstance("JKS");
        ksTrust.load(new FileInputStream("testTrust"),passPhrase);
        // KeyManager决定要使用的秘钥材料
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ksKeys,"12345".toCharArray());

        // TrustManager 决定是否允许连接
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ksTrust);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(),null);
        SSLEngine engine = sslContext.createSSLEngine("localhost",80);
        // 引擎用于客户端
        engine.setUseClientMode(true);

        //创建一个非阻塞套接字通道
        SocketChannel socketChannel = SocketChannel.open();
        //配置是否阻塞
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("hostname",80));

        //完整的连接
        while(!socketChannel.finishConnect()){
            // do something
        }

        //创建字节缓冲区以用于保存应用程序和编码数据
        SSLSession session = engine.getSession();
        ByteBuffer myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer myNetData  = ByteBuffer.allocate(session.getPacketBufferSize());
        ByteBuffer peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        ByteBuffer peerNetData  = ByteBuffer.allocate(session.getPacketBufferSize());

        //进行初始握手
//        doHandshake(socketChannel,engine,myNetData,peerNetData);
        myAppData.put("hello".getBytes());
        myAppData.flip();

    }

    public static void testSSL02() throws Exception {


    }
}
