package com.madaha.codesecone.util;

import com.google.common.base.Preconditions;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class WebUtils {

    // Get request body.
    public static String getRequestBody(HttpServletRequest request, HttpServletResponse response) throws IOException{
        InputStream in = request.getInputStream();
        return convertStreamToString(in);
    }

    public static String getRequestBody(HttpServletRequest request) throws IOException{
        InputStream in = request.getInputStream();
        return convertStreamToString(in);
    }

    // https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
    public static String convertStreamToString(java.io.InputStream is){
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    // Get request cookie name.
    public static String getCookieValueByName(HttpServletRequest request, String cookieName){
        Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, cookieName);
        return cookie == null ? null : cookie.getValue();
    }

    // htmlEscape
    public static String json2Jsoup(String callback, String jsonStr){
        return HtmlUtils.htmlEscape(callback) + "(" + jsonStr + ")";
    }


    public static String getFileExtension(String fullName){
        Preconditions.checkNotNull(fullName);
        String fileName = (new File(fullName)).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex + 1);
    }

    public static String getNameWithoutExtension(String file){
        Preconditions.checkNotNull(file);
        String fileName = (new File(file)).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }
}
