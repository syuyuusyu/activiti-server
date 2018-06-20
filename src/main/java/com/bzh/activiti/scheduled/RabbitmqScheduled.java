package com.bzh.activiti.scheduled;

import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqScheduled {

    //@Autowired
    Connection rabbitmqConn;

    public final static long ONE_SCEND=1000;
    public final static long ONE_Minute =  60 * ONE_SCEND;

    public final static long ONE_Hour =  ONE_Minute*60;

    //@Scheduled(fixedDelay=5*ONE_SCEND,initialDelay=ONE_Minute)
    public void schedulQueue(){


    }
}
