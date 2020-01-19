package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {


    public static String object2String(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            result = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> T string2Object(String string,Class<T> clazz){
        ObjectMapper objectMapper = new ObjectMapper();
        T t = null;
        try {
            t = objectMapper.readValue(string,clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }


    public static <T> List<T> string2List(String string, TypeReference<List<T>> ref){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<T> list = objectMapper.readValue(string,ref);
            return list;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    public static <T> List<T> file2List(File file, TypeReference<List<T>> ref){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<T> list = objectMapper.readValue(file,ref);
            return list;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

}
