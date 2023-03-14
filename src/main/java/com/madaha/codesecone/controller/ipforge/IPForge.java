package com.madaha.codesecone.controller.ipforge;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * IP欺骗/IP伪造
 * proxxy and noproxy
 * 通过代理失陷IP地址欺骗
 */
@RequestMapping("/ip")
@RestController
public class IPForge {

    /**
     * 获取远程访问地址
     * @param request
     * @return
     */
    @RequestMapping("/noproxy")
    public static String noProxy(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    /**
     * 以下为使用源作者代码的备注（https://github.com/JoyChou93/java-sec-code）
     * proxy_set_header X-Real-IP $remote_addr;
     * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for
     * if code used X-Forwarded-For to get ip, the code must be vulnerable.
     *
     * @param request
     * @return
     */
    @RequestMapping("proxy")
    public static String proxy(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip)){
            return ip;
        } else {
          String remoteAddr = request.getRemoteAddr();
          if (StringUtils.isNotBlank(remoteAddr)) {
              return remoteAddr;
          }
        }
        return "获取失败！";
    }
}
