package com.course2.apisecurity.api.filter.config;

import com.course2.apisecurity.api.filter.RedisAuthFilter;
import com.course2.apisecurity.api.filter.RedisTokenFilter;
import com.course2.apisecurity.repository.BasicAuthUserRepository;
import com.course2.apisecurity.service.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RedisFilterConfig {
    @Autowired
    private BasicAuthUserRepository basicAuthUserRepository;

    @Autowired
    private RedisTokenService tokenService;

    @Bean
    public FilterRegistrationBean<RedisAuthFilter> redisAuthFilter() {
        var registrationBean = new FilterRegistrationBean<RedisAuthFilter>();

        registrationBean.setFilter(new RedisAuthFilter(basicAuthUserRepository));
        registrationBean.addUrlPatterns("/api/auth/redis/v1/login");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RedisTokenFilter> redisTokenFilter() {
        var registrationBean = new FilterRegistrationBean<RedisTokenFilter>();

        registrationBean.setFilter(new RedisTokenFilter(tokenService));
        registrationBean.addUrlPatterns("/api/auth/redis/v1/time");
        registrationBean.addUrlPatterns("/api/auth/redis/v1/logout");

        return registrationBean;
    }
}
