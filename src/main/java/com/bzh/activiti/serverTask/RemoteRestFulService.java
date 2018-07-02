package com.bzh.activiti.serverTask;


import com.bzh.activiti.config.PropertiesConf;
import com.bzh.activiti.entity.InvokeEntityImpl;
import com.bzh.activiti.util.SpringUtil;
import ind.syu.restful.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通过调用远程接口进行流程业务逻辑
 */

@Service
public class RemoteRestFulService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 将调用接口的返回信息放入流程变量中
     * @param execution
     * @param invokeName
     * @param queryMap
     */
    public void setVariable(DelegateExecution execution,String invokeName,String queryMap){
        Map<String,String> strmap=parseMap(execution,queryMap);
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
        Map<String,Object> result=JSONUtil.toMap(jsonResponseEntity.getArrayJson());
        result.forEach(execution::setVariable);
    }

    /**
     * 通过调用接口将具体的业务逻辑交由具体的业务服务处理
     * @param execution
     * @param invokeName
     * @param queryMap
     */
    public void businessInvoke(DelegateExecution execution,String invokeName,String queryMap){
        Map<String,String> strmap=parseMap(execution,queryMap);
        PropertiesConf propertiesConf= (PropertiesConf) SpringUtil.getBean("propertiesConf");
        strmap.put("ispIp",propertiesConf.getIspIp());
        InvokeEntityImpl invokeEntity= (InvokeEntityImpl) SpringUtil.getBean(invokeName);
        invokeEntity.setQueryMap(strmap);
        RestFulIntergrated rfi=new RestFulIntergrated();
        final ThreadResultData trd=new ThreadResultData();
        rfi.invoke(invokeEntity,trd);
        log.info(invokeName+"接口调用发起");
    }

    private Map<String,String> parseMap(DelegateExecution execution,String queryMap){
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

        return strmap;
    }
}
