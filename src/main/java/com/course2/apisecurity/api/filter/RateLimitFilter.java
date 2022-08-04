package com.course2.apisecurity.api.filter;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RateLimitFilter extends OncePerRequestFilter {

    private RateLimiter rateLimiter;

    public RateLimitFilter(double transactionPerSecond) {

        this.rateLimiter = RateLimiter.create(transactionPerSecond);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!rateLimiter.tryAcquire()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader(HttpHeaders.RETRY_AFTER, "5");

            return;
        }

        filterChain.doFilter(request, response);
    }
}
