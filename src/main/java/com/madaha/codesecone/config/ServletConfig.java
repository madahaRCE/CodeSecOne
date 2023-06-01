package com.madaha.codesecone.config;

import com.madaha.codesecone.Servlet.AdminServlet;
import com.madaha.codesecone.Servlet.ApiServlet;
import com.madaha.codesecone.Servlet.IndexServlet;
import com.madaha.codesecone.Servlet.LoginServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ServletConfig {

    /**
     * @poc  http://10.201.170.88:28888/bypassPermission/api/../admin
     */
    @Bean
    public ServletRegistrationBean apiBypassPermissionServletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new ApiServlet(), "/bypassPermission/api");
        return bean;
    }

    @Bean
    public ServletRegistrationBean loginBypassPermissionServletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new LoginServlet(), "/bypassPermission/login");
        return bean;
    }

    @Bean
    public ServletRegistrationBean indexBypassPermissionServletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new IndexServlet(), "/bypassPermission/index");
        return bean;
    }

    @Bean
    public ServletRegistrationBean adminBypassPermissionServletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new AdminServlet(), "/bypassPermission/admin");
        return bean;
    }
}
