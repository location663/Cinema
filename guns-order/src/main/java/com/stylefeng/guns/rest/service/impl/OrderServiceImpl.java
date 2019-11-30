package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.service.OrderService;
<<<<<<< HEAD
import org.springframework.stereotype.Component;
=======
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
>>>>>>> 307eefe33bad97c0afbf1cff235444d88425455c

@Component
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    MoocOrderTMapper orderTMapper;

    @Override
    public BaseResponVO getOrderInfo(Integer nowPage, Integer pageSize, UserVO userVO) {
        Page<MoocOrderT> objectPage = new Page<>(nowPage,pageSize);
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("order_user",userVO.getUuid());
        List list = orderTMapper.selectMapsPage(objectPage, entityWrapper);
        return new BaseResponVO(0,null,list,null,null,"");
    }
}
