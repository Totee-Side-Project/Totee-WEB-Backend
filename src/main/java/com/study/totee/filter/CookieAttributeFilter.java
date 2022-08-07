//package com.study.totee.filter;
//
//import org.springframework.http.HttpHeaders;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collection;
//
//public class CookieAttributeFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        // do something
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        chain.doFilter(request, response);
//
//        addSameSite(httpServletResponse, "None");
//    }
//
//    private void addSameSite(HttpServletResponse response, String sameSite) {
//        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
//        boolean firstHeader = true;
//        for(String header : headers) {
//            if(firstHeader) {
//                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; SameSite=%s", header, sameSite));
//                firstHeader = false;
//                continue;
//            }
//            response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; SameSite=%s", header, sameSite));
//
//        }
//    }
//}
