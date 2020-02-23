package utils.http;

import org.apache.http.client.methods.*;

import java.net.URI;
import java.net.URISyntaxException;

public enum HttpMethod {
    GET,POST,HEAD,PUT,DELETE,TRACE,PATCH,OPTIONS;

    public static HttpRequestBase getRequest(String url, HttpMethod method){
        try {
            URI uri = new URI(url);
            return getRequest(uri,method);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new HttpGet(url);
    }

    public static HttpRequestBase getRequest(URI uri, HttpMethod method){
        HttpRequestBase request = null;
        switch (method){
            case GET:
                request = new HttpGet(uri);
            case PUT:
                request = new HttpPut(uri);
            case HEAD:
                request = new HttpHead(uri);
            case POST:
                request = new HttpPost(uri);
            case PATCH:
                request = new HttpPatch(uri);
            case TRACE:
                request = new HttpTrace(uri);
            case DELETE:
                request  = new HttpDelete(uri);
            case OPTIONS:
                request = new HttpOptions(uri);
            default:
                request = new HttpPost(uri);
        }
        return request;
    }
}
