package com.bzh.activiti.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.activiti.engine.ProcessEngine;

import java.io.IOException;

public class ActMsgConsumer extends DefaultConsumer{

    ProcessEngine processEngine;
    public ActMsgConsumer(Channel channel,ProcessEngine processEngine) {
        super(channel);
        this.processEngine=processEngine;
    }

    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println("Customer Received '" + message + "'");
    }

}
