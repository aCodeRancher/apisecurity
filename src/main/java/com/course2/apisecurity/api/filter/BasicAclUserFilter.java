package com.course2.apisecurity.api.filter;

import com.course2.apisecurity.repository.BasicAuthUserRepository;
import com.course2.apisecurity.server.auth.basic.BasicAuthApi;
import com.course2.apisecurity.server.util.HashUtil;
import com.course2.apisecurity.util.EncodeDecodeUtil;
import com.course2.apisecurity.util.EncryptDecryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@Order(0)
public class BasicAclUserFilter extends OncePerRequestFilter {
    @Autowired
    private BasicAuthUserRepository basicAuthUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var basicAuthString = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (isValidBasicAuth(basicAuthString)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.getWriter().write("Invalid credential");

                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidBasicAuth(String basicAuthString) throws Exception {
        if (StringUtils.isBlank(basicAuthString)) {
            return false;
        }

        var encodedAuthorizationString = StringUtils.substring(basicAuthString, "Basic".length()).trim();
        var plainAuthorizationString = EncodeDecodeUtil.decodeBase64(encodedAuthorizationString);
        var plainAuthorization = plainAuthorizationString.split(":");
        var encryptedUsername = EncryptDecryptUtil.encryptAes(plainAuthorization[0], BasicAuthApi.SECRET_KEY);
        var submittedPassword = plainAuthorization[1];

        var existingData = basicAuthUserRepository.findByUsername(encryptedUsername);

        if (existingData.isEmpty()) {
            return false;
        }

        return HashUtil.isBcryptMatch(submittedPassword, existingData.get().getPasswordHash());
    }


}
