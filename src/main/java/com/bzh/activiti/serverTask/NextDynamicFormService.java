package com.bzh.activiti.serverTask;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bzh.activiti.config.PropertiesConf;
import com.bzh.activiti.entity.InvokeEntityImpl;
import com.bzh.activiti.util.SpringUtil;
import com.sun.javafx.tk.Toolkit;
import ind.syu.restful.*;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NextDynamicFormService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 调用接口获取表单信息
     * @param execution
     * @param invokeName
     * @param queryMap
     */
    public void formVariable(DelegateExecution execution,String invokeName,String queryMap){
        System.out.println("execution = " + execution);
        Matcher m= Pattern.compile("(@(\\w+))").matcher(queryMap);
        while (m.find()){
            Object obj=execution.getVariable(m.group(2));
            if(obj!=null){
                queryMap= StringUtils.replace(queryMap,m.group(1),obj.toString());
            }else{
                log.error("queryMap:"+queryMap+"\n"+m.group(1)+" 在引擎中找不到对应的变量");
            }
        }
        Map<String,Object> map= JSONUtil.toMap(queryMap);
        Map<String,String> strmap=new HashMap<>();
        map.forEach((s,o)->{
            strmap.put(s,o.toString());
        });
        PropertiesConf propertiesConf= (PropertiesConf) SpringUtil.getBean("propertiesConf");
        strmap.put("ispIp",propertiesConf.getIspIp());
        InvokeEntityImpl invokeEntity= (InvokeEntityImpl) SpringUtil.getBean(invokeName);
        invokeEntity.setQueryMap(strmap);
        RestFulIntergrated rfi=new RestFulIntergrated();
        final ThreadResultData trd=new ThreadResultData();
        rfi.invoke(invokeEntity,trd);

        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }
        JsonResponseEntity jsonResponseEntity=trd.getResult(invokeName+"-1");
        List<Map<String,Object>> list=JSONUtil.toListMap(jsonResponseEntity.getArrayJson());
        execution.setVariable("nextForm",list);
    }

    /**
     * 调用业务相关接口
     * @param execution
     * @param invokeName
     * @param queryMap
     */
    public void businessInvoke(DelegateExecution execution,String invokeName,String queryMap){
        System.out.println("queryMap = " + queryMap);
        //${nextDynamicFormService.formVariable(execution,"userSysAccess","{username:@applyUser}")}
        //${nextDynamicFormService.businessInvoke(execution,"pushUser","{username:@applyUser,applySystemCode:@applySystemCode}")}
        Matcher m= Pattern.compile("(@(\\w+))").matcher(queryMap);
        while (m.find()){
            Object obj=execution.getVariable(m.group(2));
            if(obj!=null){
                queryMap= StringUtils.replace(queryMap,m.group(1),obj.toString());
            }else{
                log.error("queryMap:"+queryMap+"\n"+m.group(1)+" 在引擎中找不到对应的变量");
                return;
            }
        }
        Map<String,Object> map= JSONUtil.toMap(queryMap);
        Map<String,String> strmap=new HashMap<>();
        map.forEach((s,o)->{
            strmap.put(s,o.toString());
        });
        PropertiesConf propertiesConf= (PropertiesConf) SpringUtil.getBean("propertiesConf");
        strmap.put("ispIp",propertiesConf.getIspIp());
        InvokeEntityImpl invokeEntity= (InvokeEntityImpl) SpringUtil.getBean(invokeName);
        invokeEntity.setQueryMap(strmap);
        RestFulIntergrated rfi=new RestFulIntergrated();
        final ThreadResultData trd=new ThreadResultData();
        rfi.invoke(invokeEntity,trd);
        log.info(invokeName+"接口调用发起");
    }
}
