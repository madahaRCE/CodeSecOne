package com.madaha.codesecone.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 为了不要每次调用都解析 safeDomain 的xml， 所以将解析动作放在 Bean 里。
 */
@Configuration
public class SafeDomainConfig {

    private static final Logger logger = LoggerFactory.getLogger(SafeDomainConfig.class);

    /**
     * 注释： @Bean 代表将 safeDomainParser() 方法返回的对象 自动装配到 SpringIOC 容器中。
     */
    @Bean
    public SafeDomainParser safeDomainParser() {
        try {
            logger.info("safeDomainParser bean inject successfully!!!");
            return new SafeDomainParser();
        } catch (Exception e){
            logger.error("safeDomainParser is null " + e.getMessage(), e);
        }
        return null;
    }
}
