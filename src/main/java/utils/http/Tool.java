package utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;

import java.util.Iterator;
import java.util.Map;

/**
 * @author zzp
 * @date 2020/3/7
 */
public class Tool {

    /**
     * 拼接get url
     * @param url
     * @param getParams
     * @return
     */
    public static String concatGetParam(String url, Map<String,String> getParams){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url).append("?");
        Iterator<Map.Entry<String, String>> iterator = getParams.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            stringBuilder.append(next.getKey())
                    .append("=")
                    .append(next.getValue())
                    .append("&");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();
    }

    public static HttpEntity unGzip(HttpEntity entity){
        if("gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())){
            entity = new GzipDecompressingEntity(entity);
        } else if("deflate".equalsIgnoreCase(entity.getContentEncoding().getValue())){
            entity = new DeflateDecompressingEntity(entity);
        }
        return entity;
    }
}
