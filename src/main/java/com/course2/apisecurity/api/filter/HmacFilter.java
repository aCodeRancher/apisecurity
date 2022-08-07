package com.course2.apisecurity.api.filter;

import com.course2.apisecurity.api.request.util.HmacRequest;
import com.course2.apisecurity.server.util.HmacApi;
import com.course2.apisecurity.util.HmacUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.course2.apisecurity.util.HmacNonceStorage;

public class HmacFilter extends OncePerRequestFilter {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var cachedHttpRequest = new CachedBodyHttpServletRequest(request);
        var nonce = request.getHeader("X-Nonce");

        try {
            if (isValidHmac(cachedHttpRequest, nonce) && HmacNonceStorage.addWhenNotExists(nonce)) {
                filterChain.doFilter(cachedHttpRequest, response);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().print("Invalid HMAC");

                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidHmac(CachedBodyHttpServletRequest cachedHttpRequest, String nonce) throws Exception {
        var requestBody = objectMapper.readValue(cachedHttpRequest.getReader(), HmacRequest.class);
        var registerDate = cachedHttpRequest.getHeader("X-Register-Date");
        var hmacFromClient = cachedHttpRequest.getHeader("X-Hmac");

        var hmacMessage = HmacApi.constructHmacMessage(cachedHttpRequest.getMethod(), cachedHttpRequest.getRequestURI(),
                requestBody.getAmount(), requestBody.getFullName(), registerDate, nonce);

        return HmacUtil.isHmacMatch(hmacMessage, HmacApi.SECRET_KEY, hmacFromClient);
    }

}
