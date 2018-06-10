package com.bzh.activiti.config;

import com.bzh.activiti.interceptor.AuthInterceptor;
import com.bzh.activiti.util.SpringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        PropertiesConf pconf= (PropertiesConf) SpringUtil.getBean("propertiesConf");
        registry.addMapping("/**")
                .allowedOrigins(pconf.getLocalIp()+":3000")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/modelPage",
//                        "/service/**",
//                        "/repository/models/*/source",
//                        "/act/userSyn"
//                )
//                ;
//
//
//        super.addInterceptors(registry);


    }
}
