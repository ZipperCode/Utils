package utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 90604
 * @project utils
 * @date 2020/1/8
 **/
public class FileUtils {

    private static final int BUF_SIZE = 1024 * 4;

    public static String getFileExt(String fileName){
        if(ValidUtils.isEmpty(fileName)){
            return "";
        }
        String ext = "";
        int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex == -1){
            return "";
        }
        return fileName.substring(dotIndex+1).toLowerCase();
    }

    public static String getFileName(String fileName){
        if(ValidUtils.isEmpty(fileName)){
            return fileName;
        }
        Matcher matcher = Pattern.compile(".+/(.+)$").matcher(fileName);
        if(!matcher.find()){
            return fileName;
        }
        return matcher.group(1);
    }

    public static String getFileParentPath(String fileName){
        if(ValidUtils.isEmpty(fileName)){
            return fileName;
        }
        Matcher matcher = Pattern.compile(".+/").matcher(fileName);
        return (matcher.find())? matcher.group(0) : fileName;
    }

    public static boolean isExists(String fileName){
        if(ValidUtils.isEmpty(fileName)){
            return false;
        }
        File file = new File(fileName);
        return file.exists();
    }

    public static File getFile(String fileName){
        File file = new File(fileName);
        File parentFile = file.getParentFile();
        try {
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }

    public static void zeroCopy(File src, File desc){
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
            byte buff[] = new byte[BUF_SIZE];
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
            ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
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
                byte buff[] = new byte[BUF_SIZE];
                int len = 0;
                while((len = readMappedByteBuffer.remaining()) != 0){
                    if(len < BUF_SIZE){
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
        //zeroCopy  time = 3951
//        long l1 = getTime();
//        fullCopy(src,desc);
//        System.out.println("fullCopy  time = " + (getTime() - l1));

        //buffCopy time = 4221 4k 1627
//        long l2 = getTime();
//        buffCopy(src,desc);
//        System.out.println("buffCopy time = "+ (getTime() - l2));

        // channelCopy time = 4062 4k 1262
//        long l3 = getTime();
//        channelCopy(src,desc);
//        System.out.println("channelCopy time = "+ (getTime() - l3));

        // menMapCopy time = 12704  4k 3508
//        long l4 = getTime();
//        memMapCopy(src,desc);
//        System.out.println("menMapCopy time = "+ (getTime() - l4));
    }

}
