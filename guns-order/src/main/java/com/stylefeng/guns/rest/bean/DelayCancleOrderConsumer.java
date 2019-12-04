package com.stylefeng.guns.rest.bean;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

public class DelayCancleOrderConsumer {

    private DefaultMQPushConsumer consumer;

    @Value("${mq.nameserver.addr}")
    private String nameServerAddr;

    @Value("${mq.topic-order-status}")
    private String topicOrderStatus;

    @Autowired
    MoocOrderTMapper orderTMapper;

    @PostConstruct
    public void init() throws MQClientException {
        consumer.setNamesrvAddr(nameServerAddr);
        try {
            consumer.subscribe(topicOrderStatus, "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt messageExt = list.get(0);
                String s = new String(messageExt.getBody());
                HashMap map = JSON.parseObject(s, HashMap.class);
                Integer orderId = (Integer) map.get("orderId");
                MoocOrderT orderT = orderTMapper.selectById(orderId);

                // 如果订单仍未支付，则关闭订单
                if (0 == orderT.getOrderStatus()) {
                    Integer res = orderTMapper.updateStatusById(2, orderId);
                    if (1 == res) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
