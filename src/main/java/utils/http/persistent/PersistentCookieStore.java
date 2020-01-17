package utils.http.persistent;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import utils.FileUtils;
import utils.IOUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class PersistentCookieStore implements CookieStore {

    public static final String COOKIE_PREFIX = "client_";
    private String storageFileName = "D:\\a.cookie";
    private File storageFile;

    private ConcurrentHashMap<String,Cookie> clientCookie;

    private Set<Cookie> clientCookies;

    public PersistentCookieStore(String storageFileName) {
        this.storageFileName = storageFileName;
        this.storageFile = FileUtils.getFile(storageFileName);
        this.clientCookie= new ConcurrentHashMap<>(10);
        this.clientCookies =  Collections.synchronizedSet(new HashSet<Cookie>());
        initCookie();
    }

    private void initCookie(){
        try {
            FileInputStream fileInputStream = new FileInputStream(this.storageFile);
            int fileSize = fileInputStream.available();
            if(fileSize == -1 || fileSize == 0){
                return;
            }
            byte[] data = new byte[fileInputStream.available()];
            fileInputStream.read(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCookie(Cookie cookie) {
        this.clientCookies.add(cookie);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(storageFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cookie> getCookies() {
        return new ArrayList<>(this.clientCookies);
    }

    @Override
    public boolean clearExpired(Date date) {

        return false;
    }

    @Override
    public void clear() {

    }
}
