package com.madaha.codesecone.controller.ssrf;

import com.madaha.codesecone.util.HttpClientUtils;
import com.madaha.codesecone.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审计的函数
 *   1. URL
 *   2. HttpClient
 *   3. OkHttpClient
 *   4. HttpURLConnection
 *   5. Socket
 *   6. ImageIO
 *   7. DriverManager.getConnection
 *   8. SimpleDriverDataSource.getConnection
 */
@RestController
@RequestMapping("/SSRF")
public class SSRF {
    Logger log = LoggerFactory.getLogger(SSRF.class);

    /**
     * URLConnection
     * @poc http://127.0.0.1:8888/SSRF/URLConnection/vul?url=http://www.baidu.com
     * @poc http://127.0.0.1:28888/SSRF/URLConnection/vul?url=file:///Windows/System32/drivers/etc/hosts
     */
    @GetMapping("/URLConnection/vul")
    public String URLConnectionVul(String url){
        log.info("[vul] SSRF: " + url);
        return HttpClientUtils.URLConnection(url);
    }

    /**
     * SSRF绕过:
     *
     *    短链接绕过：http://127.0.0.1:8888/SSRF/URLConnection/vul2?url=http://surl-8.cn/0
     *
     *    ip进制绕过：http://127.0.0.1:8888/SSRF/URLConnection/vul2?url=http://168302434
     *       @decode http://127.0.0.1:28888/SSRF/URLConnection/vul2?url=http://10.8.23.98
     */
    @GetMapping("/URLConnection/vul2")
    public String URLConnectionVul2(String url){
        if(!SecurityUtils.isHttp(url)){
            return "不允许非http协议！！！";
        }else if (SecurityUtils.isintranet(SecurityUtils.urltoIp(url))){
            return "不允许访问内网！！！";
        }else {
            return HttpClientUtils.URLConnection(url);
        }
    }

    /**
     * safe：白名单
     */
    @GetMapping("/URLConnection/safe")
    public String URLConnectionSafe(String url){
        if(!SecurityUtils.isHttp(url)){
            return "不允许非http/https协议！！！";
        } else if (!SecurityUtils.isWhite(url)){
            return "非可信域名！";
        } else {
            return HttpClientUtils.URLConnection(url);
        }
    }

    /**
     * HTTPURLConnection
     * @poc(根本绕不过) http://127.0.0.1:28888/SSRF/HTTPURLConnection/safe?url=http://127.0.0.1:8000
     * @param url
     * @return
     */
    @GetMapping("/HTTPURLConnection/safe")
    public String HTTPURLConnectionSafe(String url){

        // 校验 url 是否以 http 或 https 开头
        if (!SecurityUtils.isHttp(url)){
            log.error("[HTTPURLConnection] 非法的 url 协议： " + url);
            return "不允许非http/https协议！！！";
        }

        // 解析 url 为 IP 地址
        String ip = SecurityUtils.urltoIp(url);
        log.info("[HTTPURLConnection] SSRF节日IP: "+ ip);

        // 校验 IP 是否为内网地址
        if(SecurityUtils.isintranet(ip)){
            log.error("[HTTPURLConnection] 不允许访问内网：" + ip);
            return "不允许访问内网！！！";
        }

        // 访问 url
        try{
            return HttpClientUtils.HTTPURLConnection(url);
        }catch (Exception e){
            log.error("[HTTPURLConnection] 访问失败： " + e.getMessage());
            return "访问失败，请稍后再试！！！";
        }

    }











}
