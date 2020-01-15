package nio;

import org.apache.commons.collections4.BagUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileCopy {

    public static void fullCopy(File src, File desc){
        FileChannel srcFileChannel = null;
        FileChannel descFileChannel = null;
        try{
            srcFileChannel = new RandomAccessFile(src,"r").getChannel();
            descFileChannel = new RandomAccessFile(desc,"rw").getChannel();
            srcFileChannel.transferTo(0,srcFileChannel.size(),descFileChannel);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(srcFileChannel != null){
                    srcFileChannel.close();
                }
                if(descFileChannel != null){
                    descFileChannel.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public static void buffCopy(File src, File desc){
        try{
            FileInputStream fileInputStream = new FileInputStream(src);
            FileOutputStream fileOutputStream = new FileOutputStream(desc);
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = fileInputStream.read(buff)) != -1){
                fileOutputStream.write(buff,0, len);
            }
            fileInputStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void channelCopy(File src, File desc){
        try{
            FileChannel srcFileChannel = new RandomAccessFile(src,"r").getChannel();
            FileChannel descFileChannel = new RandomAccessFile(desc,"rw").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(srcFileChannel.read(buffer) != -1){
                buffer.flip();
                descFileChannel.write(buffer);
                buffer.clear();
            };
            srcFileChannel.close();
            descFileChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void memMapCopy(File src, File desc){
        try{
            try{
                FileChannel srcFileChannel = new RandomAccessFile(src,"r").getChannel();
                FileChannel descFileChannel = new RandomAccessFile(desc,"rw").getChannel();
                long size = srcFileChannel.size();
                MappedByteBuffer readMappedByteBuffer = srcFileChannel.map(FileChannel.MapMode.READ_ONLY,0,size);
                MappedByteBuffer writeMappedByteBuffer = descFileChannel.map(FileChannel.MapMode.READ_WRITE,0,size);
                byte buff[] = new byte[1024];
                int len = 0;
                while((len = readMappedByteBuffer.remaining()) != 0){
                    if(len < 1024){
                        readMappedByteBuffer.get(buff,0,len);
                        writeMappedByteBuffer.put(buff,0, len );
                    }else{
                        readMappedByteBuffer.get(buff);
                        writeMappedByteBuffer.put(buff);
                    }
                }
                readMappedByteBuffer.force();
                writeMappedByteBuffer.force();
                srcFileChannel.close();
                descFileChannel.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static long getTime(){
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws IOException {
        File src = new File("D:\\AndroidProjects\\Toolkit\\work\\com.lhsgzaz.xinjigame-v1.1_1 (1).apk");
        File desc = new File("D:\\111\\q.apk");
        if(!desc.exists()){
            desc.createNewFile();
        }
        //fullCopy  time = 3951
//        long l1 = getTime();
//        fullCopy(src,desc);
//        System.out.println("fullCopy  time = " + (getTime() - l1));

        //buffCopy time = 4221
//        long l2 = getTime();
//        buffCopy(src,desc);
//        System.out.println("buffCopy time = "+ (getTime() - l2));

        // channelCopy time = 4062
//        long l3 = getTime();
//        channelCopy(src,desc);
//        System.out.println("channelCopy time = "+ (getTime() - l3));

        // menMapCopy time = 12704
        long l4 = getTime();
        memMapCopy(src,desc);
        System.out.println("menMapCopy time = "+ (getTime() - l4));
    }
}
