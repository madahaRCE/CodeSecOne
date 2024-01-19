package com.madaha.codesecone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
public class SwaggerConfig {

    /**
     * 配置之后，直接访问一些路径即可；
     * http://127.0.0.1:28888/swagger-ui/index.html
     */
    @Bean
    public Docket creatRestApi() {
        return new Docket(DocumentationType.OAS_30)      // OAS 是 OpenAPI Specification 的简称，翻译成中文就是 OpenAPI 说明书。
                .enable(true)
                .apiInfo(apiInfo())
                .groupName("开发小组名称")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.madaha.codesecone"))
                //.apis(RequestHandlerSelectors.basePackage("com.madaha.codesecone.controller"))   // 配置要扫描接口的方式
                //.apis(RequestHandlerSelectors.any())
                //.paths(PathSelectors.any())
                .build();
    }




    // 配置Swagger的默认信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("小q", "https://github.com/madahaRCE/CodeSecOne", "xxxxxxx@qq.com");
        return new ApiInfo("小q的Swagger接口文档",
                "滴水石穿，不在一日之功！",     // 描述
                "1.0",
                "https://github.com/madahaRCE/CodeSecOne",
                contact,                    // 作者信息
                "Apache 2.0",
                "https://github.com/madahaRCE/CodeSecOne",
                new ArrayList<>()
        );
    }






// 参考：https://cloud.tencent.com/developer/article/1824393
//    @Bean //配置docket以配置Swagger具体参数
//    public Docket docket(Environment environment) {
//
//        //设置swagger 环境
//        Profiles profiles = Profiles.of("dev", "test");
//        //通过environment.acceptsProfiles 判断是否自己设定在自己设定的环境中
//        boolean flag = environment.acceptsProfiles(profiles);
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apilnfo())
//                .enable(flag)
//                .select()
//                //RequestHandlerSelectors 配置要扫描的接口方式
//                //basePackage: 指定要扫描的包
//                // any() 扫描全部
//                //none() 不扫描
//                //withClassAnnotation() 扫描类上的注解
//                //withMethodAnnotation() 扫描方法上的注解
//                .apis(RequestHandlerSelectors.basePackage("com.nie.controller"))
//                //过滤
//                //.paths(PathSelectors.ant(""))
//                .build();
//    }
//
//    //配置文档信息
//    private ApiInfo apilnfo() {
//
//        Contact contact = new Contact("聂大钊", "http://xxx.xxx.com/联系人访问链接", "联系人邮箱");
//        return new ApiInfo(
//                "大钊Swagger学习", // 标题
//                "学习演示如何配置Swagger", // 描述
//                "v1.0", // 版本
//                "http://XXX/组织链接", // 组织链接
//                contact, // 联系人信息
//                "Apach 2.0 许可", // 许可
//                "许可链接", // 许可连接
//                new ArrayList<>()// 扩展
//        );
//    }
}
