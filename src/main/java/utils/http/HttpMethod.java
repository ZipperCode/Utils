package utils.http;

import org.apache.http.client.methods.*;

import java.net.URI;

public enum HttpMethod {
    GET,POST,HEAD,PUT,DELETE,TRACE,PATCH,OPTIONS;

    public static HttpRequestBase getRequest(String url, HttpMethod method){
        HttpRequestBase request = null;
        switch (method){
            case GET:
                request = new HttpGet(url);
                break;
            case PUT:
                request = new HttpPut(url);
                break;
            case HEAD:
                request = new HttpHead(url);
                break;
            case POST:
                request = new HttpPost(url);
                break;
            case PATCH:
                request = new HttpPatch(url);
                break;
            case TRACE:
                request = new HttpTrace(url);
                break;
            case DELETE:
                request  = new HttpDelete(url);
                break;
            case OPTIONS:
                request = new HttpOptions(url);
                break;
            default:
                request = new HttpPost(url);
                break;
        }
        return request;
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
