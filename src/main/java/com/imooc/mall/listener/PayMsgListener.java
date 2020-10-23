package com.imooc.mall.listener;

import com.google.gson.Gson;
import com.imooc.mall.pojo.PayInfo;
import com.imooc.mall.service.Impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//TODO 从消息队列得到支付结果
@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener implements Serializable {
    @Autowired
    OrderServiceImpl orderService;
    @RabbitHandler
    public void getPayMsg(String msg){
        log.info("接收到的消息：{}",msg);
        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if(payInfo.getPlatformStatus().equals("SUCCESS")){
            //修改订单状态 已支付
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
