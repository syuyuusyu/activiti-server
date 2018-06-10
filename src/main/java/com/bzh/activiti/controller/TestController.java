package com.bzh.activiti.controller;

import com.bzh.activiti.util.SpringUtil;
import ind.syu.restful.InvokeEntity;
import ind.syu.restful.InvokeTimeOutException;
import ind.syu.restful.RestFulIntergrated;
import ind.syu.restful.ThreadResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {



    @RequestMapping(value="/synuser1")
    public Map<String,String> synUser1(@RequestBody Map<String,Object> query){
        System.out.println("synuser1");
       return synUser(query,"s09");
    }

    @RequestMapping(value="/synuser2")
    public Map<String,String> synUser2(@RequestBody Map<String,Object> query){
        System.out.println("synuser2");
        return synUser(query,"s10");
    }

    @RequestMapping(value="/synuser3")
    public Map<String,String> synUser3(@RequestBody Map<String,Object> query){
        System.out.println("synuser3");
        return synUser(query,"s11");
    }



    public Map<String,String> synUser(Map<String,Object> query,String code){
        System.out.println("code = " + code);
        InvokeEntity synuserresult= (InvokeEntity) SpringUtil.getBean("synuserresult");
        String username= (String) query.get("username");
        Map<String,String> queryMap=new HashMap<>();
        queryMap.put("username",username);
        queryMap.put("system",code);
        synuserresult.setQueryMap(queryMap);
        RestFulIntergrated rfi=new RestFulIntergrated();
        final ThreadResultData trd=new ThreadResultData();
        rfi.invoke(synuserresult,trd);

        try {
            trd.waitForResult();
        } catch (InvokeTimeOutException e) {
            e.printStackTrace();
        }
        return queryMap;
    }
}
