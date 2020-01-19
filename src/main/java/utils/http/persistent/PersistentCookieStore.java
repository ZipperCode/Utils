package utils.http.persistent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import utils.FileUtils;
import utils.JsonUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PersistentCookieStore implements CookieStore {

    public static final String COOKIE_PREFIX = "client_";
    private String storageFileName = "D:\\a.cookie";
    private File storageFile;

    private List<Cookie> clientCookies;

    public PersistentCookieStore(String storageFileName) {
        this.storageFileName = storageFileName;
        this.storageFile = FileUtils.getFile(storageFileName);
        this.clientCookies =  new ArrayList<>();
        initCookie();
    }

    private void initCookie(){
        List<BasicClientCookie> list = getObjectForFile();
        if(list != null)
            clientCookies.addAll(list);
    }

    @Override
    public void addCookie(Cookie cookie) {
        if(cookie.isExpired(new Date()))
            return;
        this.clientCookies.add(cookie);
        writeFileData2List(this.clientCookies);

    }

    @Override
    public List<Cookie> getCookies() {
        return new ArrayList<>(this.clientCookies);
    }

    @Override
    public boolean clearExpired(Date date) {
        List<Cookie> collect = this.clientCookies.stream()
                .filter(cookie -> cookie.isExpired(new Date()))
                .collect(Collectors.toList());
        this.clientCookies.clear();
        this.clientCookies.addAll(collect);
        writeFileData2List(this.clientCookies);
        return false;
    }

    @Override
    public void clear() {
        this.clientCookies.clear();
        writeFileData2List(this.clientCookies);
    }

    private List<BasicClientCookie> getObjectForFile(){
        List<BasicClientCookie> basicClientCookies = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            basicClientCookies = objectMapper.readValue(this.storageFile, new TypeReference<List<BasicClientCookie>>() {
            });
        }catch (Exception e){
            return null;
        }
        return basicClientCookies;
    }

    private void writeFileData2List(List<Cookie> clientCookies)  {
        String s = JsonUtils.object2String(clientCookies);
        try {
            FileWriter fileWriter = new FileWriter(this.storageFile);
            fileWriter.write(s);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public BasicClientCookie convert(Cookie cookie){
        BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.getName(),cookie.getValue());
        basicClientCookie.setComment(cookie.getComment());
        basicClientCookie.setDomain(cookie.getDomain());
        basicClientCookie.setPath(cookie.getPath());
        basicClientCookie.setExpiryDate(cookie.getExpiryDate());
        basicClientCookie.setVersion(cookie.getVersion());
        return basicClientCookie;
    }
}
