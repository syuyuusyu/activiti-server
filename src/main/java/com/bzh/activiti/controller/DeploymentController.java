package com.bzh.activiti.controller;


import org.activiti.engine.RepositoryService;
import org.activiti.rest.service.api.repository.ProcessDefinitionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DeploymentController {

    @Autowired
    RepositoryService repositoryService;

    //获取当前已部署流程的列表 process-list
    @RequestMapping(value = "/process-list")
    public List<ProcessDefinitionResponse> processList(){

        return repositoryService.createProcessDefinitionQuery().list().stream().map(pd->{
            ProcessDefinitionResponse pdr=new ProcessDefinitionResponse();
            pdr.setCategory(pd.getCategory());
            pdr.setDeploymentId(pd.getDeploymentId());
            pdr.setId(pd.getId());
            pdr.setKey(pd.getKey());
            pdr.setName(pd.getName());
            pdr.setTenantId(pd.getTenantId());
            pdr.setVersion(pd.getVersion());
            return pdr;
        }).collect(Collectors.toList());
    }
}

