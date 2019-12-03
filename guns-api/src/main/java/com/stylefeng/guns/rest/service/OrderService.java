package com.stylefeng.guns.rest.service;


import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.order.OrderInfoVO;
import com.stylefeng.guns.rest.vo.user.UserVO;

public interface OrderService {

    BaseResponVO insertOrder(BuyTicketDTO buyTicketDTO, UserVO userVO);

    BaseResponVO getOrderInfo(Integer nowPage, Integer pageSize, UserVO userVO);

    String getSoldSeatsByFieldId(Integer fieldId);

    OrderInfoVO getById(Integer orderId) throws CinemaParameterException;

    Integer updateStatusById(Integer status, Integer id);
}
