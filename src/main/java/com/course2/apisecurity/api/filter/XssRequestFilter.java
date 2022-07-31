package com.course2.apisecurity.api.filter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class XssRequestFilter extends OncePerRequestFilter {

    private static final String[] XSS_REGEX = {
            "onclick|onkeypress|onkeydown|onkeyup|onerror|onchange|onmouseover|onmouseout|onblur|onselect|onfocus",
            "<\s*script\b[^>]*>(.*?)<\s*/script\b[^>]*>", "script\s+src\s*=", "<\s*script\b[^>]*>",
            "<\s*/script\b[^>]*>", "javascript.*:" };

    private List<Pattern> xssValidationPatterns;

    public XssRequestFilter() {
        xssValidationPatterns = new ArrayList<Pattern>();

        for (var xss : XSS_REGEX) {
            var pattern = Pattern.compile(xss, Pattern.CASE_INSENSITIVE);
            xssValidationPatterns.add(pattern);
        }
    }

    private boolean isXssSafe(String stringToValidate) {
        if (StringUtils.isBlank(stringToValidate)) {
            return true;
        }

        for (var pattern : xssValidationPatterns) {
            if (pattern.matcher(stringToValidate).find()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var cachedRequest = new CachedBodyHttpServletRequest(request);

        var queryString = URLDecoder.decode(
                Optional.ofNullable(cachedRequest.getQueryString()).orElse(StringUtils.EMPTY), StandardCharsets.UTF_8);
        var pathVariable = URLDecoder.decode(
                Optional.ofNullable(cachedRequest.getRequestURI()).orElse(StringUtils.EMPTY), StandardCharsets.UTF_8);
        var requestBody = IOUtils.toString(cachedRequest.getReader()).replaceAll("\\r\\n|\\r|\\n", StringUtils.EMPTY);

        if (isXssSafe(queryString) && isXssSafe(pathVariable) && isXssSafe(requestBody)) {
            filterChain.doFilter(cachedRequest, response);
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print("Bad request, XSS detected");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000");

            return;
        }
    }

}
