package utils;

import org.apache.http.util.CharArrayBuffer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static utils.DigitalUtils.*;

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

    public static void close(InputStream...inputStreams){
        if(inputStreams != null && inputStreams.length > 0){
            try{
            for (int i = 0; i < inputStreams.length; i++) {
                if(inputStreams[i] != null){
                    inputStreams[i].close();
                }
            }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void close(OutputStream...outputStreams){
        if(outputStreams != null && outputStreams.length > 0){
            try{
                for (int i = 0; i < outputStreams.length; i++) {
                    if(outputStreams[i] != null){
                        outputStreams[i].close();
                    }
                }
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

    public static void writeValidateCode(int width, int height,int length, OutputStream outputStream){
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0,width,height);
        for (int i = 0; i < (width - height) / 2; i++) {
            graphics.setColor(getRandColor());
            int x = getRandomDigital(0,width);
            int y = getRandomDigital(0,height);
            int xl = getRandomDigital(0,width);
            int yl = getRandomDigital(0,height);
            graphics.drawLine(x, y, xl,  yl);
        }
        String code = StringUtils.randomString(length);
        int fontSize = (width - 10) / length;
        int offset = (height - fontSize) / 8 ;
        for (int i = 0; i < code.length(); i++) {
            graphics.setColor(getRandColor(0,155));
            graphics.setFont(new Font("Arial",Font.ITALIC,fontSize));
            graphics.drawString(code.substring(i,i+1),(i * fontSize) + 10,getRangeDigital((height /2) + (fontSize / 3),offset));
        }
        graphics.dispose();
        try{
            ImageIO.write(bufferedImage,"PNG",outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
   }

    public static Color getRandColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r, g, b);
    }


    public static void main(String[] args) {
//        System.out.println(getFileName("f/dd/agdfg"));
//        System.out.println("parent Path : "+ getFileParentPath("C:/test/test/t.txt"));
//        System.out.println("AB:F7:FE:F9:9E:BE:82:7F:1E:21:14:1C:F8:DD:D1:EC".replaceAll(":","").toLowerCase());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("d:\\111\\code.png");
            writeValidateCode(80, 30,4,fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class A implements Serializable{
         int a = 10;
         String s = "aa";
         Date date = new Date();
         List<String> list;
         B b = new B();

         A(){
             list = new ArrayList<>();
             list.add("hello");
             list.add("hello world");
             list.add("hello nihao ");
         }

        @Override
        public String toString() {
            return "A{" +
                    "a=" + a +
                    ", s='" + s + '\'' +
                    ", date=" + date +
                    ", list=" + list +
                    '}';
        }
    }

    public static class B{
        String b = "b";

        @Override
        public String toString() {
            return "B{" +
                    "b='" + b + '\'' +
                    '}';
        }
    }

}
