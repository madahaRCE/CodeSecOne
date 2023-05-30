package com.madaha.codesecone.util;

import com.madaha.codesecone.config.WebConfig;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.http.conn.util.InetAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 存放安全类
 */
public class SecurityUtils {

    static Logger log = LoggerFactory.getLogger(SecurityUtils.class);

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
     * Determine if the URL starts with HTTP.
     * 判断是否是http类型
     *
     * @param url url
     * @return true or false
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
     * Get http url host
     *
     * @param url url
     * @return host
     */
    public static String gethost(String url){
        try {
            URI uri = new URI(url);
            return uri.getHost().toLowerCase();
        } catch (URISyntaxException e) {
            return "";
        }
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
     * Filter file path to prevent(防止) path traversal vulns.
     *
     * @param filepath file path
     * @return illegal(不合法的) file path return null
     */
    public static String pathFilter(String filepath){
        String temp = filepath;

        // use while to sovle multi urlencode
        // 判断是否存在URL编码，如果有就解码
        while (temp.indexOf('%') != -1){
            try{
                temp = URLDecoder.decode(temp, "utf-8");
            }catch (UnsupportedEncodingException e){
                log.info("Unsupported Encoding Exception: " + filepath);
                return null;
            }catch (Exception e){
                log.info(e.toString());
                return null;
            }
        }

        if (temp.contains("..") || temp.charAt(0) == '/'){
            return null;
        }

        return filepath;
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

    /**
     * 将非 <code>0-9a-1/-.<code/> 的字符替换为空
     *
     * @param str 字符串
     * @return 被过滤的字符串
     */
    public static String replaceSpecialStr(String str){
        StrBuilder strBuilder = new StrBuilder();

        str = str.toLowerCase();
        for (int i = 0; i < str.length(); i++){
            char ch = str.charAt(i);

            if(ch >= 48 && ch <= 57){     // 如果是0-9
                strBuilder.append(ch);
            }else if (ch >= 97 && ch <= 122){   // 如果是a-z
                strBuilder.append(ch);
            }else if (ch == '/' || ch == '.' || ch == '-'){
                strBuilder.append(ch);
            }
        }
        return strBuilder.toString();
    }

    /**
     * 同时支持一级域名和多级域名，相关配置在resources目录下 “/url/url_safe_domain.xml” 文件。
     * 优先判断黑名单，如果满足黑名单return null。
     *
     * @param url  the url need to check
     * @return  Safe url returns original url; Illegal url returns null;
     */
    public static String checkURL(String url) {

        if (null == url){
            return null;
        }

        ArrayList<String> safeDomains = WebConfig.getSafeDomains();
        ArrayList<String> blockDomains = WebConfig.getBlockDomains();

        try {
            String host = gethost(url);

            // 必须 http/https
            if (!isHttp(url)) {
                return null;
            }

            // 如果满足黑名单返回null
            if (blockDomains.contains(host)) {
                return null;
            }
            for (String blockDomain : blockDomains) {
                if (host.endsWith("." + blockDomain)) {
                    return null;
                }
            }

            // 支持多级域名
            if (safeDomains.contains(host)) {
                return url;
            }

            // 支持多级域名
            for (String safeDomain : safeDomains) {
                if (host.endsWith("." + safeDomain)) {
                    return url;
                }
            }

            // 不满足上述条件，直接返回null
            return null;

        } catch (NullPointerException e) {
            log.error(e.toString());
            return null;
        }
    }

}
