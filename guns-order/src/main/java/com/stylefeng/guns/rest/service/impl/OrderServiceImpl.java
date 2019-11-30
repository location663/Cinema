package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.service.OrderService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.order.OrderInfoVO;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    MoocOrderTMapper orderTMapper;

    @Reference(interfaceClass = CinemaService.class)
    CinemaService cinemaService;

    @Reference(interfaceClass = FilmService.class)
    FilmService filmService;

    @Override
    public BaseResponVO insertOrder(BuyTicketDTO buyTicketDTO) {

        OrderInfoVO orderInfoVO = new OrderInfoVO();
        MoocOrderT moocOrderT = new MoocOrderT();



        return null;
    }
}
