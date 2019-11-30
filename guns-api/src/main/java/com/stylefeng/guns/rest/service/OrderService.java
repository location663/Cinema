package com.stylefeng.guns.rest.service;

import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;

public interface OrderService {

    BaseResponVO getOrderInfo(Integer nowPage, Integer pageSize, UserVO userVO);
}
