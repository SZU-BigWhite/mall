package com.imooc.mall.controller;

import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.consts.MallConst;
import com.imooc.mall.form.UserLoginForm;
import com.imooc.mall.form.UserRegisterForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.Impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserServiceImpl userService;

    /**
     * 注册功能
     */
    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userRegisterForm) {
        User user=new User();
        BeanUtils.copyProperties(userRegisterForm,user);
        return  userService.register(user);
    }

    /**
     * 登录功能
     */
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession session) {

        ResponseVo<User> responseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        session.setAttribute(MallConst.CURRENT_USER,responseVo.getData());

        return responseVo;
    }
    /**
     * 获取用户信息
     */
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success(user);
    }
    /**
     * 退出登录
     */
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.successByMsg("退出成功");
    }


}
