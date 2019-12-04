package com.stylefeng.guns.rest.bean;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;

@Component
@Slf4j
public class PromoProducer {

    private DefaultMQProducer producer;

    @Value("${mq.nameserver.addr}")
    private String nameServerAddr;

    @Value("${mq.topic-order-status}")
    private String topicOrderStatus;


    @PostConstruct
    private void init(){
        producer = new DefaultMQProducer("promo");
        producer.setNamesrvAddr(nameServerAddr);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    public Boolean delayCancle(Integer orderId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        String s = JSON.toJSONString(map);
        Message message = new Message(topicOrderStatus, s.getBytes(Charset.forName("utf-8")));
        // this.messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        message.setDelayTimeLevel(5);
        SendResult result = null;
        try {
            result = producer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (null != result) {
            SendStatus sendStatus = result.getSendStatus();
            if (sendStatus.equals(SendStatus.SEND_OK)){
                return true;
            }
        }
        return false;
    }
}
