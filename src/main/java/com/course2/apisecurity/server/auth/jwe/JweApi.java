package com.course2.apisecurity.server.auth.jwe;

import com.course2.apisecurity.constant.JweConstant;
import com.course2.apisecurity.entity.JweData;
import com.course2.apisecurity.service.JweService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/auth/jwe/v1")
public class JweApi {

    @Autowired
    private JweService jweService;

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String time(HttpServletRequest request) {
        var encryptedUsername = request.getAttribute(JweConstant.REQUEST_ATTRIBUTE_USERNAME);

        return "Now is " + LocalTime.now() + ", accessed by " + encryptedUsername;
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    public String login(HttpServletRequest request) {
        var encryptedUsername = (String) request.getAttribute(JweConstant.REQUEST_ATTRIBUTE_USERNAME);

        var jweData = new JweData();
        jweData.setUsername(encryptedUsername);

        var jweToken = jweService.store(jweData);

        return jweToken;
    }
}
