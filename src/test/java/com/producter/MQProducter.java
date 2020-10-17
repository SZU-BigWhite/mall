package com.producter;

import com.MallApplicationTests;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;


public class MQProducter extends MallApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    //手动确认--设置prefetch 可用于限流
    @Test
    public void productUnAutoAck(){
        rabbitTemplate.convertAndSend("UnAutoQueue","UnAutoACK Message");
    }

    //topic 动态route模式
    @Test
    public void producttopic(){
        rabbitTemplate.convertAndSend("topic","user.save","user.save 消息");
    }

    //direct route模式
    @Test
    public void productDirects(){
        rabbitTemplate.convertAndSend("directs","logs","logs Message");
//        rabbitTemplate.convertAndSend("directs","error","Error Message");
    }

    //fanout 广播--发布者-订阅模式
    @Test
    public void productFanout(){
        rabbitTemplate.convertAndSend("logs","","Hello FonoutQueue");
    }

    //hello模式
    @Test
    public void productHello(){
        rabbitTemplate.convertAndSend("helloQueue","Hello testQueue");
    }

    //work模式
    @Test
    public void productWork(){
        for(int i=0;i<10;i++)
            rabbitTemplate.convertAndSend("workQueue","This is workQueue"+i);
    }


}
