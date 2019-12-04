package com.stylefeng.guns.rest.bean;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class PromoStockConsumer {

    private DefaultMQPushConsumer consumer;

    @Value("${mq.nameserver.addr}")
    private String nameServerAddr;

    @Value("${mq.topic-stock}")
    private String topicStock;


    @Autowired
    private MtimePromoStockMapper promoStockMapper;

    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer("promo");
        consumer.setNamesrvAddr(nameServerAddr);
        try {
            consumer.subscribe(topicStock, "*");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt messageExt = list.get(0);
                String s = new String(messageExt.getBody());
                HashMap hashMap = JSON.parseObject(s, HashMap.class);
                Integer promoId = (Integer) hashMap.get("promoId");
                Integer amount = (Integer) hashMap.get("amount");
                EntityWrapper<MtimePromoStock> mtimePromoStockEntityWrapper = new EntityWrapper<>();
                mtimePromoStockEntityWrapper.eq("promo_id", promoId);
                List<MtimePromoStock> mtimePromoStocks = promoStockMapper.selectList(mtimePromoStockEntityWrapper);
                MtimePromoStock mtimePromoStock = mtimePromoStocks.get(0);
//                mtimePromoStock.setStock(mtimePromoStock.getStock() - amount);
//                Integer integer = promoStockMapper.updateById(mtimePromoStock);
                Integer stock = mtimePromoStock.getStock();
                Integer integer = promoStockMapper.updateStockByPromoId(stock - amount, promoId);
                if (integer == 1) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

        });
        consumer.start();
    }
}
