package com.bzh.activiti.config;


import com.bzh.activiti.service.ActMsgConsumer;
import com.rabbitmq.client.*;
import org.activiti.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitmqConf {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${selfProperties.rabbitmqUrl}")
    String rabbitmqUrl;

    @Value("${selfProperties.queueName}")
    String queueName;



    @Bean(destroyMethod = "close")
    public Connection rabbitmqConn(){
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();


        logger.info(rabbitmqUrl);

        try {
            factory.setUri(rabbitmqUrl);
            factory.setHandshakeTimeout(50000);
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
            //channel=connection.createChannel();
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

        Consumer consumer = new ActMsgConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        return channel;
    }


}
