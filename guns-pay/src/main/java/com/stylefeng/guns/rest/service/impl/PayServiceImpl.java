package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.Main;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.PayService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Service(interfaceClass = PayService.class)
public class PayServiceImpl implements PayService {

    @Autowired
    Main alipay;

    @Override
    public BaseResponVO getPayInfo(Integer orderId) throws CinemaParameterException {
        String s = alipay.test_trade_precreate(orderId);
        int pics = s.indexOf("img");
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
}
