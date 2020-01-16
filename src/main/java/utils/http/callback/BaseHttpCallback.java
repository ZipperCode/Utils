package utils.http.callback;

import java.io.InputStream;

public interface BaseHttpCallback {

    void onSuccess(InputStream inputStream);

    void onFailure(Exception e);

    void onError(int errorCode, Exception e);

    void onCancel();
}
