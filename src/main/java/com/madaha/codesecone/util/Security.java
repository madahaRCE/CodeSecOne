package com.madaha.codesecone.util;

import org.apache.http.conn.util.InetAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 存放安全类
 */
public class Security {

    static Logger log = LoggerFactory.getLogger(Security.class);

    /**
     * 判断是否为内网IP地址
     *
     * @param ip
     * @return
     */
    public static boolean isintranet(String ip){
        log.info("isIntranet: " + ip);
        Pattern reg = Pattern.compile("^(127\\.0\\.0\\.1)|(localhost)|^(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|^(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|^(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
        Matcher match = reg.matcher(ip);
        return match.find();
    }

    /**
     * 判断是否是http类型
     *
     * @param url
     * @return
     */
    public static boolean isHttp(String url){
        return url.startsWith("http://") || url.startsWith("https://");
    }

    /**
     * 判断url是否在白名单内
     *
     * @param url
     * @return
     */
    public static boolean isWhite(String url){
        List<String> url_list = new ArrayList<String>();
        url_list.add("baidu.com");
        url_list.add("www.baidu.com");
        url_list.add("oa.baidu.com");

        // 从 url 转换 host
        URI uri = null;
        try{
            uri = new URI(url);
        }catch (URISyntaxException e){
            System.out.println(e);
        }

        /**
         * Java中有一个不太常用的关键字assert，是jdk1.4中加入的，平时开发中见的很少，不过在一些框架的源码里面的测试类里面，出现过不少它的踪迹。
         * assert意为断言的意思，这个关键字可以判断布尔值的结果是否和预期的一样，如果一样就正常执行，否则会抛出AssertionError。
         *
         * toLowerCase，计算机术语，用途是字符串中的字母被转换为小写字母。
         */
        assert uri != null;
        String host = uri.getHost().toLowerCase();

        return url_list.contains(host);
    }

    /**
     * url 转 IP
     * @param url
     * @return
     */
    public static String urltoIp(String url){
        try{
            URI uri = new URI(url);
            String host = uri.getHost().toLowerCase();

            // 判断 URL 是否为 IP 地址类型
            if(InetAddressUtils.isIPv4Address(host)){
                return host;
            }else {
                InetAddress ip = InetAddress.getByName(host);
                return ip.getHostAddress();
            }
        }catch (Exception e){
            return "127.0.0.1";
        }
    }

    /**
     * XSS 黑名单过滤
     *
     * @param content
     * @return
     */
    public static String filterXss(String content){
        content = StringUtils.replace(content, "&", "&amp;");
        content = StringUtils.replace(content, "<", "&lt;");
        content = StringUtils.replace(content, ">", "&gt;");
        content = StringUtils.replace(content, "\"", "&quot;");
        content = StringUtils.replace(content, "'", "&#x27;");
        content = StringUtils.replace(content, "/", "&#x2F;");
        return content;
    }

    public static String filterXss2(String content){

        String[] black_list = {"&", "<", ">", "\"", "'", "/"};
        String[] white_list = {"&amp;", "&lt;", "&gt;", "&quot;", "&#x27;", "&#x2F;"};

        // 遍历非法字符集合，并使用 replace 方法替换
        for (int i = 0; i < black_list.length; i++){
            content = content.replace(black_list[i], white_list[i]);
        }

        return content;
    }

    /**
     * SQL 注入检测
     *
     * @param content
     * @return
     */
    public static boolean checkSql(String content){
        String[] black_list = {"'", ";", "--", "+", ",", "%", "=", ">", "<", "*", "(", ")", "and", "or", "exec", "insert", "select", "delete", "update", "count", "drop", "chr", "mid", "master", "truncate", "char", "declare"};

        for (String s : black_list){
            if (content.toLowerCase().contains(s)){
                return true;
            }
        }

        return false;
    }

    /**
     * 目录遍历检测
     *
     * @param content
     * @return
     */
    public static boolean checkTraversal(String content){
        return content.contains("..") || content.contains("/");
    }

    /**
     *  命令执行检测
     *
     * @param content
     * @return
     */
    public static boolean chrckOs(String content){
        String[] black_list = {"|", ",", "&", "&&", ";", "||"};

        for (String s : black_list){
            if (content.contains(s)){
                return true;
            }
        }

        return false;
    }

    /**
     * XXE检测
     *
     * @param content
     * @return
     */
    public static boolean checkXXE(String content){
        String[] black_list = {"ENTITY", "DOCTYPE"};

        for (String s : black_list){
            if (content.toUpperCase().contains(s)){
                return true;
            }
        }

        return false;
    }

}