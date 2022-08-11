package com.course2.apisecurity.api.filter;

import com.course2.apisecurity.entity.AuditLogEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuditLogFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogFilter.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var cachedRequest = new CachedBodyHttpServletRequest(request);
        var requestBody = IOUtils.toString(cachedRequest.getReader());

        filterChain.doFilter(cachedRequest, response);

        var auditLogEntry = new AuditLogEntry();
        auditLogEntry.setTimestamp(LocalDateTime.now().toString());
        auditLogEntry.setPath(request.getRequestURI());
        auditLogEntry.setQuery(request.getQueryString());
        auditLogEntry.setMethod(request.getMethod());
        auditLogEntry.setRequestBody(requestBody);
        auditLogEntry.setHeaders(request.getHeader(HttpHeaders.AUTHORIZATION));

        auditLogEntry.setResponseStatusCode(response.getStatus());

        String logString = StringUtils.EMPTY;
        try {
            logString = objectMapper.writeValueAsString(auditLogEntry);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        LOG.info(logString);
    }

}
