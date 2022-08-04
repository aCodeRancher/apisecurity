package com.course2.apisecurity.api.filter;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class WhitelistIpFilter extends OncePerRequestFilter {
   // private static final String[] ALLOWED_IP = { "0:0:0:0:0:0:0:1", "10.152.33.216" };
    private static final String[] ALLOWED_IP = { "10.152.33.216" };
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!ArrayUtils.contains(ALLOWED_IP, request.getRemoteAddr())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print("Forbidden IP : " + request.getRemoteAddr());

            return;
        }

        filterChain.doFilter(request, response);
    }
}
