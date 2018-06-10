package com.bzh.activiti.business;

import com.bzh.activiti.config.PropertiesConf;
import com.bzh.activiti.util.SpringUtil;
import ind.syu.restful.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 申请平台访问权限流程调用同步用户接口
 */
public class SynUserInvoke implements JavaDelegate{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username= (String) execution.getVariable("applyUser");
        String applySystemCode= (String) execution.getVariable("applySystemCode");
        List<String> users= Arrays.stream(applySystemCode.split(",")).map(code->username+code).collect(Collectors.toList());
        execution.setVariable("users",users);
        execution.setVariable("applyTotal",applySystemCode.split(",").length);
        execution.setVariable("applyCount",0);
        execution.setVariable("user",users.stream().reduce((s1,s2)->s1+','+s2).get());
        execution.setVariable("message","");
//        InvokeEntity invokeEntity= (InvokeEntity) SpringUtil.getBean("pushUser");
//        Map<String,String> queryMap=new HashMap<>();
//        queryMap.put("username",username);
//        queryMap.put("applySystemCode",applySystemCode);
//        PropertiesConf propertiesConf= (PropertiesConf) SpringUtil.getBean("propertiesConf");
//        queryMap.put("ispIp",propertiesConf.getIspIp());
//        invokeEntity.setQueryMap(queryMap);
//        RestFulIntergrated rfi=new RestFulIntergrated();
//        final ThreadResultData trd=new ThreadResultData();
//        rfi.invoke(invokeEntity,trd);
        log.info("调用同步用户接口完成!!");


    }
}
