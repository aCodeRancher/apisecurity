package com.course2.apisecurity.api.filter;

import com.course2.apisecurity.constant.JwtConstant;
import com.course2.apisecurity.service.JwtService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
    private JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isValidJwt(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print("Invalid token");

            return;
        }
    }

    private boolean isValidJwt(HttpServletRequest request) {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.startsWithIgnoreCase(authorizationHeader, "Bearer")) {
            return false;
        }

        var bearerToken = StringUtils.substring(authorizationHeader, "Bearer".length() + 1);
        var jwtData = jwtService.read(bearerToken);

        if (jwtData.isPresent()) {
            request.setAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME, jwtData.get().getUsername());
            return true;
        } else {
            return false;
        }
    }


}
