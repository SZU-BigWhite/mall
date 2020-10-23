package com.imooc.mall.service;

import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.pojo.User;

public interface IUserService {
    /**
     * 注册
     */
    ResponseVo<User> register(User user);
    /**
     * 登录
     */
    ResponseVo<User> login(String username,String password);

}
