package test;

import com.bzh.activiti.Application;

import com.bzh.activiti.config.PropertiesConf;
import com.bzh.activiti.util.SpringUtil;
import ind.syu.restful.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestRestful {

    @Value("${selfProperties.activitiIp}")
    String activitiIp;

    @Autowired
    InvokeEntity createModels;


    @Autowired
    InvokeEntity synuserresult;

    @Test
    public void test(){
        Map<String,String> queryMap=new HashMap<>();
        queryMap.put("name","新建测试");
        queryMap.put("activitiIp",activitiIp);
        createModels.setQueryMap(queryMap);
        ThreadResultData trd=new ThreadResultData();
        RestFulIntergrated integrage=new RestFulIntergrated();
        integrage.invoke(createModels, trd);

        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }

        JsonResponseEntity jre=trd.getResult("createModels");
        String ss=jre.getArrayJson();
        System.out.println("ss = " + ss);

    }

    @Test
    public void test2(){
        InvokeEntity invokeEntity= (InvokeEntity) SpringUtil.getBean("pushUser");
        Map<String,String> queryMap=new HashMap<>();
        queryMap.put("username","admin");
        queryMap.put("applySystemCode","s09,s10,s11");
        PropertiesConf propertiesConf= (PropertiesConf) SpringUtil.getBean("propertiesConf");
        queryMap.put("ispIp",propertiesConf.getIspIp());
        invokeEntity.setQueryMap(queryMap);
        RestFulIntergrated rfi=new RestFulIntergrated();
        final ThreadResultData trd=new ThreadResultData();
        rfi.invoke(invokeEntity,trd);

        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }
        JsonResponseEntity jsonResponseEntity=trd.getResult("pushUser-1");
        Map<String,Object> invokeResult=JSONUtil.toMap(jsonResponseEntity.getArrayJson());
        boolean success= (boolean) invokeResult.get("success");
        System.out.println("success = " + success);
    }

    @Test
    public void test3(){
        Map<String,String> queryMap=new HashMap<>();
        queryMap.put("username","admin");
        queryMap.put("system","s09");
        synuserresult.setQueryMap(queryMap);
        RestFulIntergrated rfi=new RestFulIntergrated();
        final ThreadResultData trd=new ThreadResultData();
        rfi.invoke(synuserresult,trd);

        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }
        JsonResponseEntity jsonResponseEntity=trd.getResult(trd.invokeNames().get(0));
        //System.out.println("jsonResponseEntity.getArrayJson() = " + jsonResponseEntity.getArrayJson());
    }
}
