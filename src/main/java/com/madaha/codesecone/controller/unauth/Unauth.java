package com.madaha.codesecone.controller.unauth;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 未授权访问：
 *    InterceptorRegistry 拦截器未对该页面进行拦截，导致未授权访问。
 */
@RestController
@RequestMapping("/Unauth")
public class Unauth {

    /**
     * @poc http://127.0.0.1:28888/Unauth/api/info
     * @return
     */
    @GetMapping("/api/info")
    public String vul() {
        Map<String, String> map = new HashMap<>();

        map.put("name", "zhangwei");
        map.put("card", "1234567890");

        return JSON.toJSONString(map);
    }
}
