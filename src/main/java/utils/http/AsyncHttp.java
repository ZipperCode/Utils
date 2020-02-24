package utils.http;

import manager.ThreadManager;
import org.apache.http.*;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import utils.http.callback.BaseHttpResponseCallBack;
import utils.http.callback.HttpResponseCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.http.HttpConfigFactory.*;

public final class AsyncHttp {

    private String encode = DEFAULT_ENCODE;
    private int connectTimeout = -1;
    private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    private CloseableHttpAsyncClient httpAsyncClient;

    public AsyncHttp() {
        this.httpAsyncClient = HttpAsyncClients.custom()
            .setConnectionManager(getConnectionManager()) // 设置连接池管理
            .setKeepAliveStrategy(getConnectionAliveStrategy()) // 设置连接存活策略
                .setDefaultRequestConfig(getRequestConfig()) // 设置默认请求配置
                .setSSLStrategy(getSSLIOSessionStrategy())
                .setDefaultCookieStore(getCookieStore())
        .build();
        this.httpAsyncClient.start();
    }



    public void doGet(String url, HttpResponseCallback callback){
        // 添加浏览器头
        Map<String,String> header = new HashMap<>();
        header.put(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        doGet(url,header,callback,null);
    }
    public void doGet(String url, HttpResponseCallback callback, Cancellable cancellable){
        // 添加浏览器头
        Map<String,String> header = new HashMap<>();
        header.put(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        doGet(url,header,callback,cancellable);
    }

    public void doGet(String url, Map<String,String> headers, HttpResponseCallback callback, Cancellable cancellable){
        HttpGet httpGet = (HttpGet) HttpMethod.getRequest(url,HttpMethod.GET);
        for (Map.Entry<String,String> entry : headers.entrySet()){
            httpGet.addHeader(new BasicHeader(entry.getKey(),entry.getValue()));
        }
        httpGet.setCancellable(cancellable);
        request(httpGet,callback);
    }

    public void doPost(String url, List<NameValuePair> params, HttpResponseCallback callback){
        EntityBuilder entityBuilder = EntityBuilder.create();
        HttpEntity httpEntity = entityBuilder
                .setContentEncoding(encode)
                .setParameters(params)
                .setContentType(ContentType.create(
                        "application/x-www-form-urlencoded", Consts.UTF_8))
                .build();
        doPost(url,httpEntity,callback);
    }

    public void doPost(String url, HttpEntity httpEntity, HttpResponseCallback callback){
        Map<String,String> header = new HashMap<>();
        header.put(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        doPost(url,httpEntity,header,callback);
    }


    public void doPost(String url,List<NameValuePair> params, Map<String,String> headers, HttpResponseCallback callback){
        HttpPost httpPost = (HttpPost) HttpMethod.getRequest(url,HttpMethod.POST);
        if(headers != null){
            for (Map.Entry<String,String> entry : headers.entrySet()){
                httpPost.addHeader(new BasicHeader(entry.getKey(),entry.getValue()));
            }
        }
        EntityBuilder entityBuilder = EntityBuilder.create();
        HttpEntity httpEntity = entityBuilder
                .setContentEncoding(encode)
                .setParameters(params)
                .setContentType(ContentType.create(
                        "application/x-www-form-urlencoded", Consts.UTF_8))
                .build();
        httpPost.setEntity(httpEntity);
        request(httpPost,callback);
    }

    public void doPost(String url, HttpEntity httpEntity, Map<String,String> headers, HttpResponseCallback callback){
        HttpPost httpPost = (HttpPost) HttpMethod.getRequest(url,HttpMethod.POST);
        if(headers != null){
            for (Map.Entry<String,String> entry : headers.entrySet()){
                httpPost.addHeader(new BasicHeader(entry.getKey(),entry.getValue()));
            }
        }
        httpPost.setEntity(httpEntity);
        request(httpPost,callback);
    }

    private void request(HttpRequestBase request, HttpResponseCallback callback){
        httpAsyncClient.execute(request, new FutureCallback<HttpResponse>() {
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
                    try {
                        callback.onFailure(state, EntityUtils.toString(result.getEntity()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failed(Exception ex) {
                System.out.println("failed");
                System.out.println(ex);
                callback.onError(ex);
            }

            @Override
            public void cancelled() {
                System.out.println("failed");
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

    public static List<NameValuePair> paramsConvert(Map<String,String> map){
        List<NameValuePair> params = new ArrayList<>();
        if(map == null){
            return params;
        }

        for(Map.Entry<String,String> entry : map.entrySet()){
            params.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        return params;
    }

    public static void main(String[] args) {
        System.out.println("main");

        AsyncHttp asyncHttp = new AsyncHttp();
//        asyncHttp.doPost("https://httpbin.org/post",paramsConvert(map),
//               new BaseNetWorkCallBack(){
//                   @Override
//                   public void onSuccess(InputStream inputStream) {
//                       if(inputStream != null){
//                           System.out.println(IOUtils.inputStream2String(inputStream,null));
//                           try {
//                               inputStream.close();
//                           } catch (IOException e) {
//                               e.printStackTrace();
//                           }
//                       }
//                   }
//               });
        ThreadManager.executeNetWorkRunnable(()->{
            asyncHttp.doGet("https://www.baidu.com",new BaseHttpResponseCallBack());
        });
        ThreadManager.shutdownNetworkExecutor();
        System.out.println("main end ");
    }
}
