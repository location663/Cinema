package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.utils.TransferUtils;
import com.stylefeng.guns.rest.dto.BuyTicketDTO;
import com.stylefeng.guns.rest.exception.CinemaExceptionEnum;
import com.stylefeng.guns.rest.exception.CinemaParameterException;
import com.stylefeng.guns.rest.service.CinemaService;
import com.stylefeng.guns.rest.service.FilmService;
import com.stylefeng.guns.rest.service.OrderService;
import org.springframework.stereotype.Component;
import com.stylefeng.guns.rest.util.GetSeats;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.cinema.CinemaNameAndFilmIdVO;
import com.stylefeng.guns.rest.vo.order.OrderInfoVO;
import com.stylefeng.guns.rest.vo.order.SeatVO;
import com.stylefeng.guns.rest.vo.order.SeatsVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MoocOrderTMapper orderTMapper;


    @Reference(interfaceClass = CinemaService.class, check = false)
    private CinemaService cinemaService;

    @Reference(interfaceClass = FilmService.class, check = false)
    private FilmService filmService;

    @Autowired
    RedisTemplate redisTemplate;

    private String[] orderStatus = {"待支付", "已支付", "已关闭"};

    private String[] seatString = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    /**
     * 购票
     * @param buyTicketDTO
     * @return
     */
    @Override
    public BaseResponVO insertOrder(BuyTicketDTO buyTicketDTO, UserVO userVO) {

        OrderInfoVO orderInfoVO = new OrderInfoVO();
        MoocOrderT moocOrderT = new MoocOrderT();
        CinemaNameAndFilmIdVO cinemaNameAndFilmIdByFieldId = cinemaService.getCinemaNameAndFilmIdByFieldId(buyTicketDTO.getFieldId());
        moocOrderT.setCinemaId(cinemaNameAndFilmIdByFieldId.getCinemaId());
        moocOrderT.setFieldId(buyTicketDTO.getFieldId());
        moocOrderT.setFilmId(cinemaNameAndFilmIdByFieldId.getFilmId());
        moocOrderT.setSeatsIds(buyTicketDTO.getSoldSeats());

        String soldSeats = buyTicketDTO.getSoldSeats();
        String[] seats = soldSeats.split(",");

        String seatAddress = cinemaNameAndFilmIdByFieldId.getSeatAddress();
        SeatsVO seatsFromFront = (SeatsVO) redisTemplate.opsForValue().get(seatAddress);
        if (null == seatsFromFront) {
            seatsFromFront = GetSeats.getSeatsFromFront("http://localhost:1818" + seatAddress);
            redisTemplate.opsForValue().set(seatAddress, seatsFromFront);
            redisTemplate.expire(seatAddress,5 * 60, TimeUnit.HOURS);
        }
        String[] seatIds = seatsFromFront.getIds().split(",");
        SeatVO[][] single = seatsFromFront.getSingle();
        SeatVO[][] couple = seatsFromFront.getCouple();

        StringBuilder sb = new StringBuilder();
        String soldSeatsByFieldId = getSoldSeatsByFieldId(buyTicketDTO.getFieldId());
        String[] soldSeatsArray = soldSeatsByFieldId.split(",");

        for (String seat : seats) {
            for (String s : soldSeatsArray) {
                if (s.equals(seat)){
                    return new ErrorResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
                }
            }
            if (!seat.matches("^[1-9]\\d*$") || Integer.parseInt(seat) > Integer.parseInt(seatIds[seatIds.length-1])){
                return new ErrorResponVO(CinemaExceptionEnum.PARAMETER_ERROR.getStatus(), CinemaExceptionEnum.PARAMETER_ERROR.getMsg());
            }
            int seatId = Integer.parseInt(seat);
            String detailSeat = getDetailSeat(single, seatId);
            if (null != detailSeat){
                sb.append(detailSeat);
            } else if (null != getDetailSeat(couple, seatId)){
                sb.append(getDetailSeat(couple, seatId));
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        moocOrderT.setSeatsName(sb.toString());

        Integer price = cinemaNameAndFilmIdByFieldId.getPrice();
        moocOrderT.setFilmPrice(1.0 * price);
        moocOrderT.setOrderPrice(1.0 * price * seats.length);

        moocOrderT.setOrderTime(new Date());

        moocOrderT.setOrderUser(userVO.getUuid());
        moocOrderT.setOrderStatus(0);
        Integer insert = orderTMapper.insert(moocOrderT);
        if (0 == insert) {
            return new ErrorResponVO(CinemaExceptionEnum.BUYTICKETS_ERROR.getStatus(), CinemaExceptionEnum.BUYTICKETS_ERROR.getMsg());
        }
        Integer id = orderTMapper.selectLastInsertId();
        orderInfoVO.setCinemaName(cinemaNameAndFilmIdByFieldId.getCinemaName());
        orderInfoVO.setFieldTime(TransferUtils.parseDate2String2(new Date()) + " " + cinemaNameAndFilmIdByFieldId.getBeginTime());
        orderInfoVO.setFilmName(filmService.getFilmByFilmId(cinemaNameAndFilmIdByFieldId.getFilmId()).getFilmName());
        orderInfoVO.setOrderId(id.toString());
        orderInfoVO.setOrderPrice(moocOrderT.getOrderPrice().toString());
        orderInfoVO.setOrderStatus(orderStatus[moocOrderT.getOrderStatus()]);
        Long time = moocOrderT.getOrderTime().getTime();
        orderInfoVO.setOrderTimestamp(time.toString());
        orderInfoVO.setSeatsName(buyTicketDTO.getSeatsName());
        BaseResponVO baseResponVO = new BaseResponVO();
        baseResponVO.setData(orderInfoVO);
        baseResponVO.setStatus(0);
        return baseResponVO;
    }

    @Override
    public BaseResponVO getOrderInfo(Integer nowPage, Integer pageSize, UserVO userVO) {
        Page<MoocOrderT> objectPage = new Page<>(nowPage,pageSize);
        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper();
        entityWrapper.eq("order_user",userVO.getUuid());
        List<Map<String, Object>> maps = orderTMapper.selectMapsPage(objectPage, entityWrapper);
        List<OrderInfoVO> list = orderDO2OrderInfo(maps);
        return new BaseResponVO(0,null, list,null,null,"");
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        EntityWrapper<MoocOrderT> orderTEntityWrapper = new EntityWrapper<>();
        orderTEntityWrapper.eq("field_id", fieldId);
        Integer[] array = new Integer[2];
        array[0] = 0;
        array[1] = 1;
        orderTEntityWrapper.in("order_status", array);
        List<MoocOrderT> moocOrderTS = orderTMapper.selectList(orderTEntityWrapper);
        if (!moocOrderTS.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (MoocOrderT moocOrderT : moocOrderTS) {
                sb.append(moocOrderT.getSeatsIds() + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } else {
            return "";
        }
    }

    @Override
    public OrderInfoVO getById(Integer orderId) throws CinemaParameterException {
        EntityWrapper<MoocOrderT> orderTEntityWrapper = new EntityWrapper<>();
        orderTEntityWrapper.eq("uuid", orderId);
        List<MoocOrderT> moocOrderTS = orderTMapper.selectList(orderTEntityWrapper);
        if (moocOrderTS.isEmpty()){
            throw new CinemaParameterException();
        }
        MoocOrderT moocOrderT = moocOrderTS.get(0);
        OrderInfoVO orderInfoVO = new OrderInfoVO();
        Integer fieldId = moocOrderT.getFieldId();
        CinemaNameAndFilmIdVO cinemaNameAndFilmIdByFieldId = cinemaService.getCinemaNameAndFilmIdByFieldId(fieldId);
        orderInfoVO.setCinemaName(cinemaNameAndFilmIdByFieldId.getCinemaName());
        orderInfoVO.setFieldTime(TransferUtils.parseDate2String2(new Date()) + " " + cinemaNameAndFilmIdByFieldId.getBeginTime());
        orderInfoVO.setFilmName(filmService.getFilmByFilmId(cinemaNameAndFilmIdByFieldId.getFilmId()).getFilmName());
        orderInfoVO.setOrderId(orderId.toString());
        orderInfoVO.setOrderPrice(moocOrderT.getOrderPrice().toString());
        orderInfoVO.setOrderStatus(orderStatus[moocOrderT.getOrderStatus()]);
        Long time = moocOrderT.getOrderTime().getTime();
        orderInfoVO.setOrderTimestamp(time.toString());
        orderInfoVO.setFilmPrice(moocOrderT.getFilmPrice());
        String seatsIds = moocOrderT.getSeatsIds();
        String[] split = seatsIds.split(",");
        orderInfoVO.setQuantity(split.length);
        if (orderInfoVO.getOrderStatus().equals("1")){
            orderInfoVO.setOrderMsg("支付成功");
        } else {
            orderInfoVO.setOrderMsg("订单支付失败，请稍后重试");
        }
        return orderInfoVO;
    }

    @Override
    public Integer updateStatusById(Integer status, Integer id) {
        Integer res = orderTMapper.updateStatusById(status, id);
        return res;
    }

    private List<OrderInfoVO> orderDO2OrderInfo(List<Map<String, Object>> maps) {
        ArrayList<OrderInfoVO> orderInfoVOS = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            OrderInfoVO orderInfoVO = new OrderInfoVO();
            CinemaNameAndFilmIdVO fieldInfo = cinemaService.getCinemaNameAndFilmIdByFieldId((Integer) map.get("fieldId"));
            orderInfoVO.setCinemaName(fieldInfo.getCinemaName());
            orderInfoVO.setFieldTime(TransferUtils.parseDate2String2(new Date()) + " " + fieldInfo.getBeginTime());
            Integer uuid = (Integer) map.get("uuid");
            orderInfoVO.setOrderId(uuid.toString());
            Double order_price = (Double) map.get("orderPrice");
            orderInfoVO.setOrderPrice(order_price.toString());
            Integer status = (Integer) map.get("orderStatus");
            orderInfoVO.setOrderStatus(orderStatus[status]);
            Date order_time = (Date) map.get("orderTime");
            Long time = order_time.getTime() / 1000;
            orderInfoVO.setOrderTimestamp(time.toString());
            String seats_name = (String) map.get("seatsIds");
            orderInfoVO.setSeatsName(seats_name);
            orderInfoVOS.add(orderInfoVO);
        }
        return orderInfoVOS;
    }

    private String getDetailSeat(SeatVO[][] seats, int seatId){
        int temp = seatId - seats[0][0].getSeatId() + 1;
        String res = null;
        for (int i = 0; i < seats.length; i++) {
            if (temp > seats[i].length){
                temp -= seats[i].length;
            } else {
                for (int j = 0; j < seats[i].length; j++) {
                    if (seats[i][j].getSeatId() == seatId){
                        res = "第" + seatString[seats[i][j].getRow()] + "排" + seats[i][j].getColumn() + "座,";
                        return res;
                    }
                }
            }
        }
        return res;
    }
}
