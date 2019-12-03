package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.Main;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.OrderService;
import com.stylefeng.guns.rest.service.PayService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.order.OrderInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Service(interfaceClass = PayService.class)
public class PayServiceImpl implements PayService {

    @Autowired
    private Main alipay;

    @Reference(interfaceClass = OrderService.class, check = false)
    OrderService orderService;


    @Override
    public BaseResponVO getPayInfo(Integer orderId) throws CinemaParameterException {
        String s = alipay.test_trade_precreate(orderId);
        int pics = s.indexOf("pics");
        s = s.substring(pics);
        HashMap<String, String> map = new HashMap<>();
        map.put("orderId", orderId.toString());
        map.put("qRCodeAddress", s);
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(map);
        baseResponVO.setStatus(0);
        baseResponVO.setImgPre("http://localhost/");
        return baseResponVO;
    }


    @Override
    public BaseResponVO getPayResult(Integer orderId) throws CinemaParameterException {
        OrderInfoVO byId = orderService.getById(orderId);
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(byId);
        if (byId.getOrderStatus().equals("1")){
            baseResponVO.setStatus(0);
        } else {
            baseResponVO.setStatus(1);
        }
        return baseResponVO;
    }
}
