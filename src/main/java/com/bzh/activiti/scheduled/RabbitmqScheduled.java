package com.bzh.activiti.scheduled;

import com.bzh.activiti.entity.RabbitmqMessage;
import com.rabbitmq.client.Connection;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RabbitmqScheduled {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TaskService taskService;

    public final static long ONE_SCEND=1000;
    public final static long ONE_Minute =  60 * ONE_SCEND;

    public final static long ONE_Hour =  ONE_Minute*60;

    @Scheduled(fixedDelay=5*ONE_SCEND,initialDelay=20*ONE_SCEND)
    public void schedulQueue(){
        logger.info(RabbitmqMessage.RabbitQueue.size()+" ----");
        List<RabbitmqMessage> removeQ=new ArrayList<>();
        RabbitmqMessage.RabbitQueue.forEach(m->{

            if(m.getCount()>5){
                removeQ.add(m);
            }else {
                m.increaseCount();
            }

        });

        removeQ.forEach(m->{
            RabbitmqMessage.RabbitQueue.remove(m);

            List<Task> list=taskService.createTaskQuery().taskAssignee(m.getAssigneeName()).list();
            list.addAll(taskService.createTaskQuery().taskCandidateUser(m.getAssigneeName()).list());


            list.forEach(t->{
                Map<String,Object> variables=new HashMap<>();
                String msg= (String) taskService.getVariable(t.getId(),"message");
                msg=msg+";"+m.getMessage();
                logger.info(msg);
                variables.put("message",msg);
                taskService.complete(t.getId(),variables);

            });
        });


    }
}
