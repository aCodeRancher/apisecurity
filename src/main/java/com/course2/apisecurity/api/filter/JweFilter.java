package com.course2.apisecurity.api.filter;

import com.course2.apisecurity.constant.RedisConstant;
import com.course2.apisecurity.constant.SessionCookieConstant;
import com.course2.apisecurity.service.JweService;
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

public class JweFilter extends OncePerRequestFilter {
    private JweService jweService;

    public JweFilter(JweService jweService) {
        this.jweService = jweService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isValidJwe(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print("Invalid token");

            return;
        }
    }

    private boolean isValidJwe(HttpServletRequest request) {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.startsWithIgnoreCase(authorizationHeader, "Bearer")) {
            return false;
        }

        var bearerToken = StringUtils.substring(authorizationHeader, "Bearer".length() + 1);
        var jwtData = jweService.read(bearerToken);

        if (jwtData.isPresent()) {
            request.setAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME, jwtData.get().getUsername());
            return true;
        } else {
            return false;
        }
    }

}
