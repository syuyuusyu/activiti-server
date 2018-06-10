package com.bzh.activiti.serverTask;

import org.activiti.engine.EngineServices;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class TestTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        EngineServices engineServices=execution.getEngineServices();
        System.out.println("engineServices = " + engineServices);
    }
}
