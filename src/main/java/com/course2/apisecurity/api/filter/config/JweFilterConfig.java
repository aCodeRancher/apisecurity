package com.course2.apisecurity.api.filter.config;

import com.course2.apisecurity.api.filter.JweAuthFilter;
import com.course2.apisecurity.api.filter.JweFilter;
import com.course2.apisecurity.repository.BasicAuthUserRepository;
import com.course2.apisecurity.service.JweService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class JweFilterConfig {
    @Autowired
    private BasicAuthUserRepository basicAuthUserRepository;

    @Autowired
    private JweService jweService;

    @Bean
    public FilterRegistrationBean<JweAuthFilter> jweAuthFilter() {
        var registrationBean = new FilterRegistrationBean<JweAuthFilter>();

        registrationBean.setFilter(new JweAuthFilter(basicAuthUserRepository));
        registrationBean.addUrlPatterns("/api/auth/jwe/v1/login");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JweFilter> jweFilter() {
        var registrationBean = new FilterRegistrationBean<JweFilter>();

        registrationBean.setFilter(new JweFilter(jweService));
        registrationBean.addUrlPatterns("/api/auth/jwe/v1/time");
        registrationBean.addUrlPatterns("/api/auth/jwe/v1/logout");

        return registrationBean;
    }
}
