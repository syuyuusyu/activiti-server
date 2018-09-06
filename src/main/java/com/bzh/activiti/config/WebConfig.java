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
                .allowedOrigins(
                        "http://10.10.50.10:3000",
                        "http://10.10.50.14:3000",
                        "http://10.10.50.17:3000",
                        "http://localhost:3000",
                        "http://127.0.0.1:3000",
                        "http://10.10.50.9",
                        "http://10.10.50.16",
                        "http://isp.yndk.cn",
                        "http://192.168.50.20:3000",
                        "http://10.150.10.110:3000",
                        "http://59.216.201.52"
                )
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
