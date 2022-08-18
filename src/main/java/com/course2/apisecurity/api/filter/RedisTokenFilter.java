package com.course2.apisecurity.api.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.course2.apisecurity.constant.RedisConstant;
import com.course2.apisecurity.service.RedisTokenService;
public class RedisTokenFilter extends OncePerRequestFilter {

    private RedisTokenService tokenService;

    public RedisTokenFilter(RedisTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isValidRedis(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print("Invalid token");

            return;
        }
    }

    private boolean isValidRedis(HttpServletRequest request) {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.startsWithIgnoreCase(authorizationHeader, "Bearer")) {
            return false;
        }

        var bearerToken = StringUtils.substring(authorizationHeader, "Bearer".length() + 1);
        var token = tokenService.read(bearerToken);

        if (token.isPresent()) {
            request.setAttribute(RedisConstant.REQUEST_ATTRIBUTE_USERNAME, token.get().getUsername());
            return true;
        } else {
            return false;
        }
    }
}
