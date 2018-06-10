package com.bzh.activiti.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConfigurationProperties(prefix="selfProperties")
@Order(value=1)
public class PropertiesConf {

    private String localIp;
    private String actitvitiIp;
    private String ispIp;

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getActitvitiIp() {
        return actitvitiIp;
    }

    public void setActitvitiIp(String actitvitiIp) {
        this.actitvitiIp = actitvitiIp;
    }

    public String getIspIp() {
        return ispIp;
    }

    public void setIspIp(String ispIp) {
        this.ispIp = ispIp;
    }
}
