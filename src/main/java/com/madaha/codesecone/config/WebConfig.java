package com.madaha.codesecone.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * Solve can't get value in filter by @Value when not using embed tomcat.
 * 解决在不使用嵌入 tomcat 时无法通过@Value在过滤器中获取值。
 *
 * @author JoyChou @2019-07-24
 */

// 注解@Component表明WebConfig类将被SpringIoC容器扫描装配，并且Bean名称为webConfig
@Component
public class WebConfig {

    private static String businessCallback;
    private static ArrayList<String> safeDomains = new ArrayList<>();
    private static ArrayList<String> blockDomains = new ArrayList<>();


    @Value("${codesecone.business.callback}")
    public void setBusinessCallback(String businessCallback) {
        WebConfig.businessCallback = businessCallback;
    }

    public static String getBusinessCallback() {
        return businessCallback;
    }

    void setSafeDomains(ArrayList<String> safeDomains) {
        WebConfig.safeDomains = safeDomains;
    }

    public static ArrayList<String> getSafeDomains() {
        return safeDomains;
    }

    void setBlockDomains(ArrayList<String> blockDomains) {
        WebConfig.blockDomains = blockDomains;
    }

    public static ArrayList<String> getBlockDomains() {
        return blockDomains;
    }
}
