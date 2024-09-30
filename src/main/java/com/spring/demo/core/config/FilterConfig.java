package com.spring.demo.core.config;

import com.spring.demo.core.filter.UrlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UrlFilter> urlValidationFilter() {
        FilterRegistrationBean<UrlFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new UrlFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

}
