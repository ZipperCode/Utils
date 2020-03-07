package qqsd;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.util.EntityUtils;
import utils.http.AsyncHttp;
import utils.http.Tool;
import utils.http.callback.HttpResponseCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zzp
 * @date 2020/3/7
 */
public class Main {

    private static final String _10028 = "10028";// 评论
    private static final String _10000 = "10000";//帖子列表

    public static void main(String[] args) {
        getSubjectById("5625444");
    }

    public static void getSubjectById(String id){

        String url = "https://i.qqshidao.com/api/index.php";

        Map<String,String> header = new HashMap<>();
        header.put("AppRegfromFirst","Web");
        header.put("AppVersion","4.5.4");
        header.put("Accept","*/*");
        header.put("User-Agent","qiuqiushidao/4.5.4 (Linux; U; Android 5.1.1; HUAWEI MLA-AL10 Build/HUAWEIMLA-AL10)");
        header.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        header.put("Connection","Keep-Alive");
        header.put("Accept-Encoding","gzip");
        Map<String,String> param = new HashMap<>();
        param.put("c_ck","20ec9b331124c021e2470efd7003a19d");
//        param.put("c_key","6e8d20c1f7355e459ba25caa2d4fa518");  // 观点
         param.put("c_key","74a15bd7247d9cd55a6dc13bbdcd432c");  // 评论列表
        param.put("threadid",id);
        param.put("order","1");
        param.put("dict","-1");
        param.put("pid","0");

        Map<String,String> getParam = new HashMap<>();
        getParam.put("suid","c8d8c4d6d948ad01b4745b5a8bae6a60");
        getParam.put("quid","386401");
        getParam.put("imei","863064652324619");
        getParam.put("mac","38:D5:47:B6:2E:7E");
        getParam.put("c_type","2");
        getParam.put("c_cpid","2");
        getParam.put("c_id",_10028);

        AsyncHttp http = new AsyncHttp();
        String postUrl = Tool.concatGetParam(url,getParam);
        http.doPost(postUrl, param, header, new HttpResponseCallback() {
            @Override
            public void onSuccess(HttpEntity entity) {
                System.out.println(entity.getContentEncoding().getValue());
                System.out.println(entity.isStreaming());
                System.out.println(entity.getContentType().getValue());
                if(entity.getContentEncoding()!=null){
                    if("gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())){
                        entity = new GzipDecompressingEntity(entity);
                    } else if("deflate".equalsIgnoreCase(entity.getContentEncoding().getValue())){
                        entity = new DeflateDecompressingEntity(entity);
                    }}
                try {
                    String result = EntityUtils.toString(entity, "UTF-8");// 取出应答字符串
                    System.out.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                System.out.println("code = "+ code + "msg = "+ msg);
            }

            @Override
            public void onError(Exception e) {
                System.err.println(e);
            }

            @Override
            public void onCancel() {
                System.out.println("cancel");
            }
        });

    }
}
