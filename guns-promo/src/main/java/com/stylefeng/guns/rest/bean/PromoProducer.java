package com.stylefeng.guns.rest.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.rest.service.PromoService;
import com.stylefeng.guns.rest.vo.promo.PromoStockLogStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;

@Component
@Slf4j
public class PromoProducer {

    private DefaultMQProducer producer;

    private TransactionMQProducer transactionMQProducer;



    @Value("${mq.nameserver.addr}")
    private String nameServerAddr;

    @Value("${mq.group-producer}")
    private String groupProducer;

    @Value("${mq.topic-stock}")
    private String topicStock;

    @Value("${mq.topic-promo-transaction}")
    private String topicPromoTransaction;


    @Autowired
    private PromoService promoService;


    /**
     * 普通消息生产者初始化
     */
    @PostConstruct
    private void init(){
        producer = new DefaultMQProducer(groupProducer);
        producer.setNamesrvAddr(nameServerAddr);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        transactionMQProducer = new TransactionMQProducer("transactionGroup");
        transactionMQProducer.setNamesrvAddr(nameServerAddr);
        try {
            transactionMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        // 事务监听回调器
        transactionMQProducer.setTransactionListener(new TransactionListener() {

            // 执行本地事务
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
                HashMap<String, Object> map = (HashMap) arg;
                Integer promoId = (Integer) map.get("promoId");
                Integer amount = (Integer) map.get("amount");
                Integer userId = (Integer) map.get("userId");
                String stockLogId = (String) map.get("stockLogId");
                Boolean order4Producer = null;

                try {
                    order4Producer = promoService.createOrder4Producer(promoId, amount, userId, stockLogId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                if (order4Producer){
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            // 回查本地事务
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                String s = new String(messageExt.getBody());
                HashMap map = JSON.parseObject(s, HashMap.class);
                String stockLogId = (String) map.get("stockLogId");
                Integer stockLogStatusByUuid = promoService.getStockLogStatusByUuid(stockLogId);
                // 流水表的状态为失败，返回给MQ Server rollback
                if (PromoStockLogStatusEnum.STOCK_LOG_FAILED.getValue().equals(stockLogStatusByUuid)){
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                // 流水表的状态为成功，返回给MQ Server commit
                if (PromoStockLogStatusEnum.STOCK_LOG_SUCCESS.getValue().equals(stockLogStatusByUuid)) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.UNKNOW;
            }
        });
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

    /**
     * mq事务生成秒杀订单，扣减库存
     * @param promoId
     * @param amount
     * @param userId
     * @return
     */
    public Boolean createOrderInTransaction(Integer promoId, Integer amount, Integer userId, String stockLogId){

        HashMap<String, Object> msgMap = new HashMap<>();
        msgMap.put("promoId", promoId);
        msgMap.put("amount", amount);
        msgMap.put("userId", userId);
        msgMap.put("stockLogId", stockLogId);

        HashMap<String, Object> argMap = new HashMap<>();
        argMap.put("promoId", promoId);
        argMap.put("amount", amount);
        argMap.put("userId", userId);
        argMap.put("stockLogId", stockLogId);

        String s = JSON.toJSONString(msgMap);
        Message message = new Message(topicStock, s.getBytes(Charset.forName("utf-8")));

        TransactionSendResult transactionSendResult = null;
        try {
            // 发送事务消息，执行本地事务
            transactionSendResult = transactionMQProducer.sendMessageInTransaction(message, argMap);
        } catch (MQClientException e) {
            log.error("事务消息发送失败");
            e.printStackTrace();
            return false;
        }

        LocalTransactionState localTransactionState = transactionSendResult.getLocalTransactionState();
        if (null != localTransactionState ){
            if ( LocalTransactionState.COMMIT_MESSAGE.equals(localTransactionState)){
                return true;
            }
        }
        return false;
    }
}
