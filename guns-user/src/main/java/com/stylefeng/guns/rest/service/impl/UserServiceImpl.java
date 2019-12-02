package com.stylefeng.guns.rest.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.exception.CinemaException;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.rest.dto.UserRegisterDTO;
import com.stylefeng.guns.rest.service.UserService;
import com.stylefeng.guns.rest.vo.BaseResponVO;
import com.stylefeng.guns.rest.vo.ErrorResponVO;
import com.stylefeng.guns.rest.vo.user.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    MtimeUserTMapper userTMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public BaseResponVO userRegister(UserRegisterDTO user) {

        EntityWrapper<MtimeUserT> mtimeUserTEntityWrapper = new EntityWrapper<>();
        mtimeUserTEntityWrapper.eq("user_name",user.getUsername());
        Integer count = userTMapper.selectCount(mtimeUserTEntityWrapper);
        if (count != 0){
            return new BaseResponVO(1,null,null,null,null,"用户已存在");
        }

        MtimeUserT mtimeUserT = new MtimeUserT();
        mtimeUserT.setUserName(user.getUsername());
        mtimeUserT.setUserPwd(user.getPassword());
        mtimeUserT.setEmail(user.getEmail());
        mtimeUserT.setUserPhone(user.getMobile());
        mtimeUserT.setAddress(user.getAddress());
        mtimeUserT.setBeginTime(new Date());
        mtimeUserT.setUpdateTime(new Date());

        Integer insert = userTMapper.insert(mtimeUserT);

        return new BaseResponVO(0,null,null,null,null,"注册成功");
    }

    /**
     * 用户名验证
     * @param username
     * @return
     */
    @Override
    public BaseResponVO userCheck(String username) {

        EntityWrapper<MtimeUserT> mtimeUserTEntityWrapper = new EntityWrapper<>();
        mtimeUserTEntityWrapper.eq("user_name",username);
        Integer count = userTMapper.selectCount(mtimeUserTEntityWrapper);
        if (count != 0){
            return new BaseResponVO(1,null,null,null,null,"用户已经注册");
        }
        return new BaseResponVO(0,null,null,null,null,"用户名不存在");
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public UserVO auth(String username, String password) {

        if (username == null && password == null){
            return null;
        }

        EntityWrapper<MtimeUserT> mtimeUserTEntityWrapper = new EntityWrapper<>();
        mtimeUserTEntityWrapper.eq("user_name",username);
        List<MtimeUserT> mtimeUserTS = userTMapper.selectList(mtimeUserTEntityWrapper);
        if (mtimeUserTS == null){
            return null;
        }

        UserVO userVO = null;
        for (MtimeUserT mtimeUserT : mtimeUserTS) {
            if (password.equals(mtimeUserT.getUserPwd())){
                userVO = new UserVO();
                BeanUtils.copyProperties(mtimeUserT, userVO);
            }
        }
        return userVO;

    }

    /**退出登录
     * @param token
     * @return
     * @throws CinemaException
     */
    @Override
    public BaseResponVO userLogout(String token) throws CinemaException {
        //先判断token
        Object o = redisTemplate.opsForValue().get(token);
        if(o == null){
            throw new CinemaException(1, "退出失败，用户尚未登陆");
        }
        //确定token存在，从redis中删除以token为key,userVO为value的数据
        Boolean delete = redisTemplate.delete(token);
        if(delete == false){
            throw new CinemaException(999,"系统出现异常，请联系管理员");
        }
        //从redis中删除以userName为key,token为value的数据
        UserVO userVO = (UserVO) o;
        Boolean delete1 = redisTemplate.delete(userVO.getUserName());
        if(delete1 == false){
            throw new CinemaException(999,"系统出现异常，请联系管理员");
        }
        return new BaseResponVO(0, "成功退出");
    }

    /**用户查询
     * @param token
     * @return
     * @throws CinemaException
     */
    @Override
    public BaseResponVO getUserInfo(String token) throws CinemaException {
        Object o = redisTemplate.opsForValue().get(token);
        if(o == null){
//            throw new CinemaException(1, "查询失败，用户尚未登陆");
            return new ErrorResponVO(700, "未登录");
        }
        UserVO userVO = (UserVO) o;
        return new BaseResponVO(0, null, userVO, null, null, null);
    }

    /**修改用户信息
     * @param token
     * @param userVO
     * @return
     * @throws CinemaException
     */
    @Override
    public BaseResponVO updateUserInfo(String token, UserVO userVO) throws CinemaException {
        Object o = redisTemplate.opsForValue().get(token);
        if(o == null){
            throw new CinemaException(1, "更新失败，用户尚未登陆");
        }
        UserVO userVO1 = (UserVO) o;
        if(userVO.getUuid() != userVO1.getUuid()){
            throw new CinemaException(2,"严重警告，你在搞事");
        }
        MtimeUserT mtimeUserT = userTMapper.selectById(userVO.getUuid());
        BeanUtils.copyProperties(userVO, mtimeUserT);

        mtimeUserT.setUpdateTime(new Date());
        //待定

        //把信息在mysql数据库中更新
        Integer update = userTMapper.updateAllColumnById(mtimeUserT);
        //如果信息不修改，再更新数据库的对应这条数据，update会返回0，但实际用户信息修改并没有失败
//        if(update == 0){
//            throw new CinemaException(1, "用户信息修改失败");
//        }
        //塞进redis
        redisTemplate.opsForValue().set(token, mtimeUserT);
        return new BaseResponVO(0, null, mtimeUserT, null,null, null);
    }
}
