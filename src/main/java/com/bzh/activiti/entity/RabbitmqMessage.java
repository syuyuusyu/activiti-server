package com.bzh.activiti.entity;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class RabbitmqMessage {

    public  static List<RabbitmqMessage> RabbitQueue = new Vector<>();

    private int count;
    private String assigneeName;
    private String message;
    private String type;

    public int getCount() {
        return count;
    }

    public void increaseCount(){
        count++;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RabbitmqMessage{" +
                "count=" + count +
                ", assigneeName='" + assigneeName + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
