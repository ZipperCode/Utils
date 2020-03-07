package utils.http.callback;

import exception.NetWorkException;
import org.apache.http.HttpEntity;

import java.io.InputStream;

public interface HttpResponseCallback {

    void onSuccess(HttpEntity httpEntity);

    void onFailure(int code, String msg);

    void onError(Exception e);

    void onCancel();
}
