package com.bzh.activiti.service;

import com.bzh.activiti.entity.RabbitmqMessage;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import ind.syu.restful.JSONUtil;
import org.activiti.engine.ProcessEngine;

import java.io.IOException;
import java.util.Optional;

public class ActMsgConsumer extends DefaultConsumer {

    ProcessEngine processEngine;

    public ActMsgConsumer(Channel channel) {
        super(channel);
        System.out.println("ActMsgConsumer inital" );
    }

    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        String message = new String(body, "UTF-8");
        RabbitmqMessage rmg = JSONUtil.JsonToBean(RabbitmqMessage.class, message);
        System.out.println("rsg = " + rmg);

        Optional<RabbitmqMessage> awaitMsg = RabbitmqMessage.RabbitQueue
                .stream().filter(m -> m.getAssigneeName().equals(rmg.getAssigneeName())).findFirst();
        if (!awaitMsg.isPresent()) {
            RabbitmqMessage.RabbitQueue.add(rmg);
        } else {
            if ("complate".equals(rmg.getType())) {
                RabbitmqMessage.RabbitQueue.add(rmg);
                RabbitmqMessage.RabbitQueue.remove(awaitMsg.get());
            }
        }


    }

}
