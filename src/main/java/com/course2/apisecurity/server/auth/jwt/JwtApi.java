package com.course2.apisecurity.server.auth.jwt;

import com.course2.apisecurity.constant.JwtConstant;
import com.course2.apisecurity.entity.JwtData;
import com.course2.apisecurity.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;


@RestController
@RequestMapping("/api/auth/jwt/v1")
public class JwtApi {

    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String time(HttpServletRequest request) {
        var encryptedUsername = request.getAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME);

        return "Now is " + LocalTime.now() + ", accessed by " + encryptedUsername;
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(JwtConstant.REQUEST_ATTRIBUTE_USERNAME);

        var jwtData = new JwtData();
        jwtData.setUsername(encryptedUsername);

        var jwtToken = jwtService.store(jwtData);

        return jwtToken;
    }
}
