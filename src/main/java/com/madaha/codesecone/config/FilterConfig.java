package com.madaha.codesecone.config;

import com.madaha.codesecone.Filter.BypassPermissionFilter;
import com.madaha.codesecone.Filter.TestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;


@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean testFilterRegistrationBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        // 设置匹配路由
        bean.addUrlPatterns("/filter/test");
        // 设置优先级
        bean.setOrder(1);
        // 绑定过滤器
        bean.setFilter(new TestFilter());
        return bean;
    }

    @Bean
    public FilterRegistrationBean bypassPermissionFilterRegistrationBean() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.addUrlPatterns("/bypassPermission/*");
        bean.setOrder(2);
        bean.setFilter(new BypassPermissionFilter());
        return bean;
    }

}
