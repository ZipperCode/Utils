package utils;

import org.apache.http.util.CharArrayBuffer;

import java.io.*;
import java.util.Date;

public class IOUtils {
    private final static String ENCODING = "UTF-8";

    public static String inputStream2String(InputStream inputStream,String encoding){
        if(inputStream == null){
            return "";
        }
        String encode = StringUtils.isEmpty(encoding) ? ENCODING: encoding;
        try {
            CharArrayBuffer charArrayBuffer = new CharArrayBuffer(inputStream.available());
            Reader reader = new InputStreamReader(inputStream,encode);
            char buf[] = new char[1024];
            int len = -1;
            while((len = reader.read(buf)) != -1){
                charArrayBuffer.append(buf,0,len);
            }
            return charArrayBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void closeStream(InputStream inputStream, OutputStream outputStream){
        try{
            if(inputStream != null){
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void close(InputStream inputStream){
        if(inputStream != null){
            try{
                inputStream.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void close(OutputStream outputStream){
        if(outputStream != null){
            try{
                outputStream.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public static byte[] serialize(Serializable object){
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();;
        byte data[] = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(objectOutputStream);
            close(byteArrayOutputStream);
        }
        return data;
    }

    public static <T> T unSerializable(byte data[], Class<T> clazz){
        ObjectInputStream objectInputStream = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        T t = null;
        try{
            objectInputStream  = new ObjectInputStream(byteArrayInputStream);
            t = (T)objectInputStream.readObject();
        }catch (IOException | ClassNotFoundException e ){
            e.printStackTrace();
        }finally {
            close(byteArrayInputStream);
            close(objectInputStream);
        }
        return t;
    }

    public static Serializable unSerializable(byte data[]){
        ObjectInputStream objectInputStream = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Serializable obj = null;
        try{
            objectInputStream  = new ObjectInputStream(byteArrayInputStream);
            obj = (Serializable) objectInputStream.readObject();
        }catch (IOException | ClassNotFoundException | ClassCastException e ){
            e.printStackTrace();
        }finally {
            close(byteArrayInputStream);
            close(objectInputStream);
        }
        return obj;
    }

    public static void main(String[] args) {
//        System.out.println(getFileName("f/dd/agdfg"));
//        System.out.println("parent Path : "+ getFileParentPath("C:/test/test/t.txt"));
        System.out.println("AB:F7:FE:F9:9E:BE:82:7F:1E:21:14:1C:F8:DD:D1:EC".replaceAll(":","").toLowerCase());
        byte[] serialize = serialize(new A());

        A a = (A) unSerializable(serialize);
        System.out.println(a);

    }

    public static class A implements Serializable{
         int a = 10;
         String s = "aa";
         Date date = new Date();

        @Override
        public String toString() {
            return "A{" +
                    "a=" + a +
                    ", s='" + s + '\'' +
                    ", date=" + date +
                    '}';
        }
    }

}
