package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.utils.TransferUtils;
import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.service.OrderService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaNameAndFilmIdVO;
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
        CinemaNameAndFilmIdVO cinemaNameAndFilmIdByFieldId = cinemaService.getCinemaNameAndFilmIdByFieldId(buyTicketDTO.getFieldId());

        moocOrderT.setFieldId(buyTicketDTO.getFieldId());
        moocOrderT.setFilmId(cinemaNameAndFilmIdByFieldId.getFilmId());
        Integer price = cinemaNameAndFilmIdByFieldId.getPrice();
        moocOrderT.setFilmPrice(1.0 * price);
        String soldSeats = buyTicketDTO.getSoldSeats();
        String[] seats = soldSeats.split(",");
        moocOrderT.setOrderPrice(1.0 * price * seats.length);

        return null;
    }
}
