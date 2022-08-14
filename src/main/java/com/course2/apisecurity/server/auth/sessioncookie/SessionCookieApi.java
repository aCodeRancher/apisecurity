package com.course2.apisecurity.server.auth.sessioncookie;

import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course2.apisecurity.constant.SessionCookieConstant;
import com.course2.apisecurity.entity.SessionCookieToken;
import com.course2.apisecurity.service.SessionCookieTokenService;

@RestController
@RequestMapping("/api/auth/session-cookie/v1")
public class SessionCookieApi {

    @Autowired
    private SessionCookieTokenService tokenService;

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);
        var token = new SessionCookieToken();
        token.setUsername(encryptedUsername);

        var tokenId = tokenService.store(request, token);

      //  return tokenId;
        return "Logged in";
    }

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String time(HttpServletRequest request) {
        var encryptedUsername = request.getAttribute(SessionCookieConstant.REQUEST_ATTRIBUTE_USERNAME);

        return "Now is " + LocalTime.now() + ", accessed by " + encryptedUsername;
    }

    @DeleteMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
    public String logout(HttpServletRequest request) {
        tokenService.delete(request);
        return "Logged out";
    }

}
