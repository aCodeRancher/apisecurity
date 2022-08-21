package com.course2.apisecurity.api.filter.config;

import com.course2.apisecurity.api.filter.SessionCookieTokenFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import com.course2.apisecurity.api.filter.SessionCookieAuthFilter;
import com.course2.apisecurity.repository.BasicAuthUserRepository;
import com.course2.apisecurity.service.SessionCookieTokenService;

//@Configuration
public class SessionCookieFilterConfig {

    @Autowired
    private BasicAuthUserRepository basicAuthUserRepository;

    @Autowired
    private SessionCookieTokenService tokenService;

    @Bean
    public FilterRegistrationBean<SessionCookieAuthFilter> sessionCookieAuthFilter() {
        var registrationBean = new FilterRegistrationBean<SessionCookieAuthFilter>();

        registrationBean.setFilter(new SessionCookieAuthFilter(basicAuthUserRepository));
        registrationBean.addUrlPatterns("/api/auth/session-cookie/v1/login");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SessionCookieTokenFilter> sessionCookieTokenFilter() {
        var registrationBean = new FilterRegistrationBean<SessionCookieTokenFilter>();

        registrationBean.setFilter(new SessionCookieTokenFilter(tokenService));
        registrationBean.addUrlPatterns("/api/auth/session-cookie/v1/time");
        registrationBean.addUrlPatterns("/api/auth/session-cookie/v1/logout");

        return registrationBean;
    }
}
