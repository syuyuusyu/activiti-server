package com.bzh.activiti.serverTask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * 平台访问权限流程调用被申请平台同步用户接口后记录调用的接口是否完成了回调
 */
@Component
public class ApplyCountCompleteListener implements TaskListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask) {
        DelegateExecution execution=delegateTask.getExecution();
        synchronized (execution){
            Integer applyCount = (Integer)execution.getVariable("applyCount");
            System.out.println("applyCount = " + applyCount);
            Integer a=applyCount+1;
            System.out.println("a = " + a);
            execution.setVariable("applyCount", a );
        }
//        synchronized (delegateTask){
//            Integer applyCount = (Integer) delegateTask.getExecution().getVariable("applyCount");
//            System.out.println("applyCount = " + applyCount);
//            Integer a=applyCount+1;
//            delegateTask.getExecution().setVariable("applyCount", a );
//        }


    }

}

