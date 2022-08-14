package com.course2.apisecurity.service;

import com.course2.apisecurity.constant.SessionCookieConstant;
import org.springframework.stereotype.Service;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import com.course2.apisecurity.entity.SessionCookieToken;
import com.course2.apisecurity.util.HashUtil;


@Service
public class SessionCookieTokenService {
    public String store(HttpServletRequest request, SessionCookieToken token) {
        var session = request.getSession(true);

        if (session != null) {
            session.invalidate();
        }

        session = request.getSession(true);

        session.setAttribute(SessionCookieConstant.SESSION_ATTRIBUTE_USERNAME,  token.getUsername());

        try {
            return HashUtil.sha256(session.getId(), token.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public Optional<SessionCookieToken> read(HttpServletRequest request) {
        var session = request.getSession(false);

        if (session == null) {
            return Optional.empty();
        }

        var username = (String) session.getAttribute(SessionCookieConstant.SESSION_ATTRIBUTE_USERNAME);

            var token = new SessionCookieToken();
            token.setUsername(username);

         return Optional.of(token);


    }

    public void delete(HttpServletRequest request) {
        var session = request.getSession(false);
        session.invalidate();
    }
}