package com.bzh.activiti.controller;


import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FormController {

    @Autowired
    FormService formService;

    @Autowired
    RepositoryService repositoryService;

    //获取启动表单
    @RequestMapping("/startFrom/{processDefinitionId}")
    public List<FormProperty> startFrom(@PathVariable("processDefinitionId") String pdid){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pdid).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();
        StartFormData formData = formService.getStartFormData(pdid);

        // 从请求中获取表单字段的值
        List<FormProperty> formProperties = formData.getFormProperties();
        return formProperties;
    }


}
