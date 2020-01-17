package io;

import java.io.File;
import java.nio.ByteBuffer;

public interface FileCallBack {

    void onSuccess(ByteBuffer byteBuffer);

    void onError(Throwable e);
}
