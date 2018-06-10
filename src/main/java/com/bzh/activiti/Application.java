package com.bzh.activiti;

import com.bzh.activiti.config.PropertiesConf;
import com.bzh.activiti.entity.InvokeEntityImpl;
import com.bzh.activiti.serverTask.NextDynamicFormService;
import com.bzh.activiti.util.SpringUtil;
import ind.syu.restful.InvokeTimeOutException;
import ind.syu.restful.JsonResponseEntity;
import ind.syu.restful.RestFulIntergrated;
import ind.syu.restful.ThreadResultData;
import org.activiti.engine.EngineServices;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@ComponentScan(basePackages={"com.bzh.activiti"})
@EnableScheduling
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class,
        //org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration.class,
        //org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
        //org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
