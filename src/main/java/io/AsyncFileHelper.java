package io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 暂时没用
 */
public class AsyncFileHelper {

    public static void main(String[] args) {
        Path path = Paths.get("D:\\111\\1.txt");
//        try {
//            AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
//            ByteBuffer buffer = ByteBuffer.allocate(1024);
//            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
//                @Override
//                public void completed(Integer integer, ByteBuffer byteBuffer) {
//                    System.out.println("completed");
//                    System.out.println(byteBuffer);
//                }
//
//                @Override
//                public void failed(Throwable throwable, ByteBuffer byteBuffer) {
//                    System.out.println("failed");
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        final long l1 = System.currentTimeMillis();
        Path p2 = Paths.get("D:\\111\\2.txt");
        copy(path, p2, new FileCallBack() {
            @Override
            public void onSuccess(ByteBuffer byteBuffer) {
                System.out.println("complete !!!" + (System.currentTimeMillis() - l1));
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error !!!");
            }
        });
        System.out.println("main end");
    }

    public static void copy(Path srcPath, Path descPath, FileCallBack fileCallBack){
        read(srcPath, new FileCallBack() {
            @Override
            public void onSuccess(ByteBuffer byteBuffer) {
                write(descPath,byteBuffer,fileCallBack);
                System.out.println("success");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }
        });
    }

    public static void read(Path srcPath, FileCallBack fileCallBack){
        try{
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(srcPath, StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer integer, ByteBuffer byteBuffer) {
                    System.out.println("completed==>" + integer);
                    fileCallBack.onSuccess(byteBuffer);
                }

                @Override
                public void failed(Throwable throwable, ByteBuffer byteBuffer) {
                    System.out.println("failed");
                    fileCallBack.onError(throwable);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void write(Path descPath, ByteBuffer buffer, FileCallBack fileCallBack){
        try{
            AsynchronousFileChannel writeChannel = AsynchronousFileChannel.open(descPath,StandardOpenOption.APPEND, StandardOpenOption.WRITE);
            writeChannel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer integer, ByteBuffer byteBuffer) {
                    System.out.println("completed==>");
                    fileCallBack.onSuccess(null);
                }

                @Override
                public void failed(Throwable throwable, ByteBuffer byteBuffer) {
                    fileCallBack.onError(throwable);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
