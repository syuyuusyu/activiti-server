package com.bzh.activiti.controller;


import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.rest.service.api.runtime.task.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TaskController {

    @Autowired
    IdentityService identityService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;


    @RequestMapping(value = "/userTask/{userId}",method = RequestMethod.GET)
    @ResponseBody
    public List<TaskResponse> userTask(@PathVariable(name="userId") String userId){

        List<Task> list=taskService.createTaskQuery().taskAssignee(userId).list();
        list.addAll(taskService.createTaskQuery().taskCandidateUser(userId).list());


        return list.stream()
//                .map(T->{
//                    T.setName(userId+""+T.getName());
//                    return T;
//                })
                .map(TaskResponse::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/userTask/list",method = RequestMethod.GET)
    @ResponseBody
    public List<TaskResponse> teskList(){


        return taskService.createTaskQuery().list()
                .stream().map(TaskResponse::new)
                .map(t->{
                    String taskId=t.getId();

                    return t;
                })
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/userTask/submit/{taskId}",method = RequestMethod.POST)
    public Map<String,Object> submitTask( @RequestBody Map<String,Object> requestMap,@PathVariable(name="taskId") String taskId){
        Map<String,Object> map=new HashMap<>();
        try {
            taskService.complete(taskId,requestMap);
            map.put("success",true);
            map.put("msg","办理成功,流程进入下一环节");
            if (requestMap.containsKey("isLast")){
                boolean isLast= (boolean) requestMap.get("isLast");
                if(isLast){
                    map.put("msg","办理成功,流程已全部完成");
                }
            }
        }catch (Exception e){
            map.put("success",false);
            map.put("msg",e.getMessage());
        }
        return map;

    }

    @RequestMapping(value="/userTask/variables/{taskId}/{variableName}")
    @ResponseBody
    public Map<String,Object> variables(@PathVariable(name="taskId") String taskId,
                                              @PathVariable(name="variableName") String variableName){
        Map<String,Object> map=new HashMap<>();
        map.put("data",taskService.getVariable(taskId,variableName));
        return map;
    }

    @RequestMapping(value="/userTask/processDefinitionKey/{taskId}")
    @ResponseBody
    public Map<String,String> getProcessDefinitionKey(@PathVariable(name="taskId") String taskId){
        Map<String,String> map=new HashMap<>();
        Task task= taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance pi= runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        map.put("key",pi.getProcessDefinitionKey());
        return map;
    }

}

