package com.bzh.activiti.config;

import com.bzh.activiti.form.DynamicForm;
import com.bzh.activiti.form.JavascriptFormType;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyBeanPostProcessor implements BeanPostProcessor {

    public MyBeanPostProcessor(){
        super();
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("springProcessEngineConfiguration")){
            SpringProcessEngineConfiguration pc=(SpringProcessEngineConfiguration)bean;
            List<AbstractFormType> types=new ArrayList<>();
            types.add(new JavascriptFormType());
            types.add(new DynamicForm());
            pc.setCustomFormTypes(types);
            pc.setActivityFontName("Menlo");
            pc.setLabelFontName("Menlo");
            
            return pc;
        }
        return bean;
    }
}
