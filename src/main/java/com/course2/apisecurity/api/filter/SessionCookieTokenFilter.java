package com.course2.apisecurity.api.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.course2.apisecurity.constant.SessionCookieConstant;
import com.course2.apisecurity.service.SessionCookieTokenService;

public class SessionCookieTokenFilter extends OncePerRequestFilter {
    private SessionCookieTokenService tokenService;

    public SessionCookieTokenFilter(SessionCookieTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isValidSessionCookie(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print("Invalid token");

            return;
        }
    }

    private boolean isValidSessionCookie(HttpServletRequest request) {
        //var providedTokenId = request.getHeader("X-CSRF");
        var token = tokenService.read(request);

        if (token.isPresent()) {
            request.setAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME, token.get().getUsername());
            return true;
        } else {
            return false;
        }
    }


}
