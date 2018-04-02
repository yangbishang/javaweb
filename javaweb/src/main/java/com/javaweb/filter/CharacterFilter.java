package com.javaweb.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 字符过滤器: 特殊的 Servlet
 */
public class CharacterFilter implements Filter {
    private FilterConfig filterConfig; // 过滤器的配置信息
    private String encoding; // 字符编码


    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        // 根据过滤器的配置信息获取指定参数的值
        this.encoding = this.filterConfig.getInitParameter("encoding");
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest.getCharacterEncoding() == null) {
            // 在请求对象中设置字符编码
            servletRequest.setCharacterEncoding(encoding);
        }
        // 继续执行后续的操作
        filterChain.doFilter(servletRequest, servletResponse);
    }


    public void destroy() {
        this.filterConfig = null;
    }
}
