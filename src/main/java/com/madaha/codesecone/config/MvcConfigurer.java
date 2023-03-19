package com.madaha.codesecone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * # 接口实现与配置
 * # WebMvcConfigurer
 * #   addInterceptors：拦截器
 * #   addViewControllers：页面跳转
 * #   addResourceHandlers：静态资源
 * #   configureDefaultServletHandling：默认静态资源处理器
 * #   configureViewResolvers：视图解析器
 * #   configureContentNegotiation：配置内容裁决的一些参数
 * #   addCorsMappings：CORS跨域
 * #   configureMessageConverters：信息转换器
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

    /**
     * 拦截器配置
     *
     * InterceptorRegistry 内的addInterceptor需要一个实现HandlerInterceptor接口的拦截器实例，addPathPatterns方法用于设置拦截器的过滤路径规则。
     * addPathPatterns("/**") 拦截所有 controller 请求
     * excludePathPatterns（） 设置特定 controller 请求不进行拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/captcha", "/login", "/user/login", "/user/ldap", "/img/**", "/css/**","/js/**", "/Unauth/**");
                // 使用拦截器“excludePathPatterns”过滤“thymeleaf"静态文件时，设置路径为 “/img/**” 即可，无需加入父路径。 亲测~~
    }



    /**
     * 视图控制器配置
     *
     * 最经常用到的就是"/"、"/index"路径请求时不通过@RequestMapping配置，而是直接通过配置文件映射指定请求路径到指定View页面；
     * 当然也是在请求目标页面时 “不需要做什么数据处理” 才可以这样使用。
     * 这样设置就是为了 在不需要 controller 进行数据处理的时候，仍然能够访问到静态页面。
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 浏览器输入访问地址路径，返回 thymeleaf 的html模板至前段界面上。
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/login").setViewName("/login");
        registry.addViewController("/index").setViewName("/index");
//        registry.addViewController("/index/sqli").setViewName("/sqli");

    }



//    /**
//     * 跨域支持
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")  //浏览器允许所有的域访问       // 注意 * 不能满足带有cookie的访问,Origin 必须是全匹配
//                .allowCredentials(true)   // 允许带cookie访问
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("token")
//                .maxAge(3600);
//    }



//    /**
//     * 配置请求视图映射
//     */
//    @Bean
//    public InternalResourceViewResolver resourceViewResolver()
//    {
//        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
//        //请求视图文件的前缀地址
//        internalResourceViewResolver.setPrefix("/WEB-INF/jsp/");
//        //请求视图文件的后缀
//        internalResourceViewResolver.setSuffix(".jsp");
//        return internalResourceViewResolver;
//    }
//
//    /**
//     * 视图配置
//     */
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        super.configureViewResolvers(registry);
//        registry.viewResolver(resourceViewResolver());
//        /*registry.jsp("/WEB-INF/jsp/",".jsp");*/
//    }

}
