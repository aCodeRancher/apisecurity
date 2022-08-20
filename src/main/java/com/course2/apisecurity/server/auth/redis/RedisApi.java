package com.course2.apisecurity.server.auth.redis;

import com.course2.apisecurity.constant.RedisConstant;
import com.course2.apisecurity.entity.RedisToken;
import com.course2.apisecurity.service.RedisTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

//@RestController
//@RequestMapping("/api/auth/redis/v1")
public class RedisApi {
    @Autowired
    private RedisTokenService tokenService;

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String time(HttpServletRequest request) {
        var encryptedUsername = request.getAttribute(RedisConstant.REQUEST_ATTRIBUTE_USERNAME);

        return "Now is " + LocalTime.now() + ", accessed by " + encryptedUsername;
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(RedisConstant.REQUEST_ATTRIBUTE_USERNAME);
        var token = new RedisToken();
        token.setUsername(encryptedUsername);

        var tokenId = tokenService.store(token);

        return tokenId;
    }

    @DeleteMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
    public String logout(@RequestHeader(required = true, name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        var token = StringUtils.substring(authorizationHeader, "Bearer".length() + 1);
        tokenService.delete(token);
        return "Logged out";
    }
}
