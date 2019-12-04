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

    @Value("${mq.topic-stock}")
    private String topicStock;

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

    /**
     * 发送减少mysql库存的消息
     * @param promoId
     * @param amount
     * @return
     */
    public Boolean decreaseStock(Integer promoId, Integer amount){
        HashMap<String, Object> map = new HashMap<>();
        map.put("promoId", promoId);
        map.put("amount", amount);
        String s = JSON.toJSONString(map);
        Message message = new Message(topicStock, s.getBytes(Charset.forName("utf-8")));
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
        if (result != null) {
            SendStatus sendStatus = result.getSendStatus();
            if (SendStatus.SEND_OK.equals(sendStatus)){
                return true;
            }
        }
        return false;
    }

}
