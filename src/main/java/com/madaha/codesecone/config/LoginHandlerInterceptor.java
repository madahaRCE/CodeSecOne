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

    public static int number_i = 0;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 测试：标注一下，拦截器运行了
        System.out.println("Interceptor preHandler method is running !"  + "  ----  [第：" +   ++number_i   + "次request请求！]");

        /**
         * 测试-查看调用过程（Springboot的Interceptor，类似Servlet技术中的Filter）：
         *      （1）设置断点调试测试；
         *      （2）先进入 WebMvcConfigurer接口 的 addInterceptors() 方法；
         *      （3）然后再通过 InterceptorRegistry.addInterceptor()方法进行 HandlerInterceptor 的实例化；
         *      （4）最终 preHandle() 执行 拦截功能。
         *
         *  注：在springboot内存马利用过程中，可以通过 addInterceptor() 方法，进行动态注册 Interceptor拦截器 的恶意实例。
         */


        /**
         * 验证session是否有效：
         *    （1）获取登录session对象，并进行判断是否已登录。 （注意：只有添加了具体实现，才能够进行拦截。）
         *    （2）如果session可用 则继续执行逻辑； 如果session不存在 则需要重新登录 来获取session。 （注意：此处只是验证了是否存在可用的session。）
         */
        Object session = request.getSession().getAttribute("LoginUser");

        // 注意！在正常登录过程中，应该对此处的session进行验证；（个人感觉~ 可以通过查询数据库进行验证）
        if (session == null){
            request.setAttribute("msg","请先登录~~");
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        }else {
            return true;
        }


        // 如果没有具体实现，仍需要return返回结果，否则就阻断了。
//         return true;
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

/**
 * 在 Spring MVC 框架中定义一个拦截器需要对拦截器进行定义和配置，主要有以下 2 种方式：
 *    （1）通过实现 HandlerInterceptor 接口或继承 HandlerInterceptor 接口的实现类（例如 HandlerInterceptorAdapter）来定义
 *    （2）通过实现 WebRequestInterceptor 接口或继承 WebRequestInterceptor 接口的实现类来定义
 *
 * Interceptor示例（这里我们选择继承HandlerInterceptor接口来实现一个Interceptor。）
 * HandlerInterceptor接口有三个方法，如下：
 *      （1）preHandle：该方法在控制器的处理请求方法前执行，其返回值表示是否中断后续操作，返回 true 表示继续向下执行，返回 false 表示中断后续操作。
 *      （2）postHandle：该方法在控制器的处理请求方法调用之后、解析视图之前执行，可以通过此方法对请求域中的模型和视图做进一步的修改。
 *      （3）afterCompletion：该方法在控制器的处理请求方法执行完成后执行，即视图渲染结束后执行，可以通过此方法实现一些资源清理、记录日志信息等工作。
 */