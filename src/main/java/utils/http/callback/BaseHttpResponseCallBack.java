package utils.http.callback;

import java.io.IOException;
import java.io.InputStream;

public class BaseHttpResponseCallBack implements HttpResponseCallback {

    @Override
    public void onSuccess(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        System.out.println("响应失败 ==> code = " + code + " msg = "+ msg);
    }

    @Override
    public void onError(Exception e){
    }

    @Override
    public void onCancel() {
    }
}
