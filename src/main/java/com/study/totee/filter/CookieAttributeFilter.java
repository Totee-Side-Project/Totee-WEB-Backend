package com.study.totee.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
public class CookieAttributeFilter implements Filter{


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        chain.doFilter(request, response);
        log.info("필터동작");
        addSameSite(httpServletResponse , "None");

    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    private void addSameSite(HttpServletResponse response, String sameSite) {

        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;
        for (String header : headers) { // there can be multiple Set-Cookie attributes
            if (firstHeader) {
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
                firstHeader = false;
                continue;
            }
            log.info("필터동작");
            response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; %s", header, "SameSite=" + sameSite));
            log.info("과연 : {}", response);
            log.info("어때? : {}", response.getHeader(HttpHeaders.SET_COOKIE));
        }

    }

}