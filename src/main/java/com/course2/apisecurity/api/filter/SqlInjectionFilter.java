package com.course2.apisecurity.api.filter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
public class SqlInjectionFilter extends OncePerRequestFilter {

    private static final String[] SQL_REGEX = { "(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)",
            "(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)" };

    private List<Pattern> sqlValidationPatterns;

    public SqlInjectionFilter() {
        sqlValidationPatterns = new ArrayList<Pattern>();

        for (var sqlStatement : SQL_REGEX) {
            var pattern = Pattern.compile(sqlStatement, Pattern.CASE_INSENSITIVE);
            sqlValidationPatterns.add(pattern);
        }
    }

    private boolean isSqlInjectionSafe(String stringToValidate) {
        if (StringUtils.isBlank(stringToValidate)) {
            return true;
        }

        for (var pattern : sqlValidationPatterns) {
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

        if (isSqlInjectionSafe(queryString) && isSqlInjectionSafe(pathVariable) && isSqlInjectionSafe(requestBody)) {
            filterChain.doFilter(cachedRequest, response);
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().print("Bad request, SQL injection detected");

            return;
        }
    }

}
