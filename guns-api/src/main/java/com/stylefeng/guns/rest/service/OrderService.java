package com.stylefeng.guns.rest.service;


import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;

public interface OrderService {

    BaseResponVO insertOrder(BuyTicketDTO buyTicketDTO, UserVO userVO);

    BaseResponVO getOrderInfo(Integer nowPage, Integer pageSize, UserVO userVO);

    String getSoldSeatsByFieldId(Integer fieldId);
}
