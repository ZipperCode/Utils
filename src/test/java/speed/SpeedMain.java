package speed;

import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import utils.http.AsyncHttp;
import utils.http.callback.NetWorkCallback;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author 90604
 * @project utils
 * @date 2020/2/11
 **/
public class SpeedMain {

    public static void main(String[] args) {
        AsyncHttp asyncHttp = new AsyncHttp();
        List<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("buid", String.valueOf(new Date().getTime())));
        param.add(new BasicNameValuePair("type", "1"));
        param.add(new BasicNameValuePair("source", "m"));
        param.add(new BasicNameValuePair("trial", "1"));
        Map<String,String> headers = new HashMap<>();
        headers.put("Connection","keep-alive");
        headers.put("Accept","*/*");
        headers.put("Origin","http://www.speedtest.cn");
        headers.put(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
        headers.put("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        headers.put("Referer","https://www.speedtest.cn/speedupm/upload");
//        headers.put("Accept-Encoding","gzip,deflate");
//        headers.put("Accept-Language","zh-CN,en-US,q=0.8");

        asyncHttp.doPost("http://tisu-api.speedtest.cn/api/v2/speedup/open",param,headers, new NetWorkCallback() {
            @Override
            public void onSuccess(InputStream inputStream) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null){
                        System.out.println(line);
                    }
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                System.out.println("failure code= "+ code+" , msg = " + msg);
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onCancel() {

            }
        });
    }
}
