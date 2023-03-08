package com.madaha.codesecone.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Interceptor 拦截器实现，实例化类
 * 此处是为了配置登录拦截器的实现
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 测试：标注一下，拦截器运行了
        System.out.println("Interceptor preHandler method is running !");

//        /**
//         * 设置断点调试测试：
//         * 先进入 WebMvcConfigurer接口 的 addInterceptors() 方法；
//         * 然后再通过 InterceptorRegistry.addInterceptor()方法进行 HandlerInterceptor 的实例化；
//         * 最终 preHandle() 执行 拦截功能。
//         */
//        System.out.println("1");
//        System.out.println("22");
//        System.out.println("333");
//        System.out.println("4444");
//        System.out.println("55555");


        /**
         * 获取登录session对象，并进行判断是否已登录。
         * 注意：只有添加了具体实现，才能够进行拦截。
         */
//        Object session = request.getSession().getAttribute("LoginUser");
//        if (session == null){
//            request.setAttribute("msg","请先登录");
//            request.getRequestDispatcher("/login").forward(request, response);
//            return false;
//        }else {
//            return true;
//        }


        // 如果没有具体实现，仍需要return返回结果，否则就阻断了。
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 此接口方法暂未使用，此处只是为了先加上，为后续学习做提醒使用
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 此接口方法暂未使用，此处只是为了先加上，为后续学习做提醒使用
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

