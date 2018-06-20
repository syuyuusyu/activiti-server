package com.bzh.activiti.config;


import com.bzh.activiti.service.ActMsgConsumer;
import com.rabbitmq.client.*;
import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

//@Configuration
public class RabbitmqConf {

    @Value("${selfProperties.rabbitmqUrl}")
    String rabbitmqUrl;

    @Value("${selfProperties.queueName}")
    String queueName;

    @Autowired
    ProcessEngine processEngine;

    @Bean(destroyMethod = "close")
    public Connection rabbitmqConn(){
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ相关信息
        //factory.setHost(rabbitmqUrl);

        try {
            factory.setUri(rabbitmqUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        //创建一个新的连接
        Connection connection=null;
        Channel channel=null;

        try {
            connection=factory.newConnection();
            channel=connection.createChannel();
            //channel.exchangeDelete("");
//            channel.queueDeclare(queueName, false, false, false, null);
//            Consumer consumer = new ActMsgConsumer(channel);
//            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return connection;
    }

    @Bean(destroyMethod="close")
    public Channel channel(Connection rabbitmqConn) throws IOException {
        //Connection rabbitmqConn=rabbitmqConn();
        Channel channel=null;

        channel=rabbitmqConn.createChannel();
        //channel.exchangeDelete("");
        channel.queueDeclare(queueName, false, false, false, null);

        String message = "hello world!";
        //6.通过channel向队列中添加消息，第一个参数是转发器，使用空的转发器（默认的转发器，类型是direct）
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("message = " + message);

        //Consumer consumer = new ActMsgConsumer(channel,processEngine);
        //channel.basicConsume(queueName, true, consumer);
        return channel;
    }


}
