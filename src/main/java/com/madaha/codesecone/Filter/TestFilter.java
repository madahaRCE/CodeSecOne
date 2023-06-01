package com.madaha.codesecone.Filter;

import javax.servlet.*;
import java.io.IOException;

public class TestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("TestFilter success!");
        // 要继续处理请求，必须要添加 FilterChain.doFilter()
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
