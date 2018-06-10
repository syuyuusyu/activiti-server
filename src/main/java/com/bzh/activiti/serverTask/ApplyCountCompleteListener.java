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

    public void notify(DelegateTask delegateTask) {

        Integer applyCount = (Integer) delegateTask.getVariable("applyCount");
        System.out.println("applyCount = " + applyCount);
        delegateTask.setVariable("applyCount", applyCount + 1);
    }

    public void notify(DelegateExecution execution) throws Exception {
        Integer applyCount = (Integer) execution.getVariable("applyCount");
        System.out.println("applyCount = " + applyCount);
        execution.setVariable("applyCount", applyCount + 1);
    }
}

