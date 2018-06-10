package com.bzh.activiti.entity;


import ind.syu.restful.InvokeCompleteEvent;
import ind.syu.restful.InvokeEntity;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class InvokeEntityImpl implements InvokeEntity {

    private Integer id;

    private String name;

    private String descrption;

    private String url;

    private String body;

    private String head;

    private String parseFun;

    private String method;

    private String next;

    private Map<String,String> queryMap;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getBody() {
        if(StringUtils.isEmpty(body)){
            return "{}";
        }
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getHead() {
        if(StringUtils.isEmpty(head)){
            return "{}";
        }
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getParseFun() {
        return parseFun;
    }

    @Override
    public Map<String, String> getQueryMap() {
        return queryMap;
    }


    public void setParseFun(String parseFun) {
        this.parseFun = parseFun;
    }

    @Override
    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public void setQp(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }


    @Override
    public List<InvokeEntity> next() {
        return null;
    }

    @Override
    public InvokeCompleteEvent invokeCompleteEvent() {
        return null;
    }

    @Override
    public Map<String, Object> transferMap() {
        return null;
    }
}
