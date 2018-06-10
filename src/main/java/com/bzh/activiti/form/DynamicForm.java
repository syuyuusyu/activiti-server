package com.bzh.activiti.form;

import ind.syu.restful.JSONUtil;
import org.activiti.engine.form.AbstractFormType;

import java.util.List;
import java.util.Map;

public class DynamicForm extends AbstractFormType {
    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        System.out.println("propertyValue = " + propertyValue);
        List<Map<String,Object>> list=JSONUtil.toListMap(propertyValue);
        return list;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        System.out.println("convertModelValueToFormValue");
        List<Map<String,Object>>  list= (List<Map<String, Object>>) modelValue;
        String result="[";
        for(int i=0;i<list.size();i++){
            if(i==list.size()-1){
                result=result+JSONUtil.mapToJson(list.get(i));
            }else{
                result=result+JSONUtil.mapToJson(list.get(i))+",";
            }
        }
        result=result+"]";
        System.out.println("result = " + result);
        return result;
    }

    @Override
    public String getName() {
        return "dynamicForm";
    }
}
