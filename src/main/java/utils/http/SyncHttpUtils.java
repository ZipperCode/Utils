package utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import utils.http.callback.BaseHttpCallback;

import javax.security.auth.callback.Callback;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SyncHttpUtils {

    CloseableHttpClient httpClient = HttpClients.createDefault();


    public String doGet(String url){
        String result = null;
        try{
            HttpGet get = new HttpGet(url);
            ResponseHandler<String> responseHandler = (HttpResponse response)->{
                int state = response.getStatusLine().getStatusCode();
                if(state >= 200 && state < 300){
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity):null;
                }else{
                    throw new ClientProtocolException("响应错误 state = "+state);
                }
            };
            result = httpClient.execute(get,responseHandler);
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }


    public String doGet(URI uri){
        String result = null;
        try{
            HttpGet get = new HttpGet(uri);
            ResponseHandler<String> responseHandler = (HttpResponse response)->{
                int state = response.getStatusLine().getStatusCode();
                if(state >= 200 && state < 300){
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity):null;
                }else{
                    throw new ClientProtocolException("响应错误 state = "+state);
                }
            };
            result = httpClient.execute(get,responseHandler);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    public void proxyGet(String url,HttpHost proxyHost){
        try {
//            HttpHost target = new HttpHost("httpbin.org", 443, "https");
//            HttpHost proxy = new HttpHost("127.0.0.1", 8080, "http");

            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxyHost)
                    .build();
            HttpGet request = new HttpGet(url);
            request.setConfig(config);

            System.out.println("Executing request " + request.getRequestLine() + " to " + request.getHeaders("host") + " via " + proxyHost);

            CloseableHttpResponse response = this.httpClient.execute(request);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void proxyAuthenticationGet(String url, HttpHost proxyHost, Map<AuthScope, Credentials> credentialsMap){
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        Iterator<Map.Entry<AuthScope, Credentials>> iterator = credentialsMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<AuthScope,Credentials> entry = iterator.next();
            credsProvider.setCredentials(entry.getKey(),entry.getValue());
        }
//        credsProvider.setCredentials(
//                new AuthScope("localhost", 8888),
//                new UsernamePasswordCredentials("squid", "squid"));
        try {
            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxyHost)
                    .build();
            HttpGet httpget = new HttpGet(url);
            httpget.setConfig(config);

            System.out.println("Executing request " + httpget.getRequestLine() + " to " + httpget.getHeaders("host") + " via " + proxyHost);

            CloseableHttpResponse response = this.httpClient.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
