package com.bzh.activiti.controller;

import com.bzh.activiti.config.PropertiesConf;
import com.bzh.activiti.util.SpringUtil;
import ind.syu.restful.InvokeEntity;
import ind.syu.restful.InvokeTimeOutException;
import ind.syu.restful.RestFulIntergrated;
import ind.syu.restful.ThreadResultData;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.rest.service.api.runtime.task.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

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


//        return identityService.createGroupQuery().groupMember(userId).list()
//                .stream()
//                .map(G->G.getId())
//                .map(G->taskService.createTaskQuery().taskCandidateGroup(G).list()).reduce(
//                        taskService.createTaskQuery().taskCandidateUser(userId).list(),
//                        (l1,l2)->{l1.addAll(l2);return l1;}
//                        )
//                .stream().map(TaskResponse::new).collect(Collectors.toList());
        System.out.println("taskService.createTaskQuery().taskCandidateUser(userId).list().size() = " + taskService.createTaskQuery().taskCandidateUser(userId).list().size());
        return taskService.createTaskQuery().taskCandidateUser(userId).list()
                .stream().map(TaskResponse::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/userTask/submit/{taskId}",method = RequestMethod.POST)
    public Map<String,Object> submitTask( @RequestBody Map<String,Object> requestMap,@PathVariable(name="taskId") String taskId){
        Map<String,Object> map=new HashMap<>();
        try {
            taskService.complete(taskId,requestMap);
            map.put("success",true);
            map.put("msg","办理成功,流程进入下一环节");
            if (requestMap.containsKey("message")){
                String msg= (String) requestMap.get("message");
                if("complete".equals(msg)){
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

