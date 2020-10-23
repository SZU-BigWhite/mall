package com.imooc.mall.controller;

import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.consts.MallConst;
import com.imooc.mall.form.ShippingAddForm;
import com.imooc.mall.form.ShippingGetForm;
import com.imooc.mall.form.ShippingUpdateForm;
import com.imooc.mall.service.Impl.ShippingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class ShippingController {
    @Autowired
    ShippingServiceImpl shippingService;
    @PostMapping("/shippings")
    public ResponseVo add(@Valid @RequestBody ShippingAddForm shippingAddForm,
                          HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.add(user.getId(),shippingAddForm);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable("shippingId") Integer shippingId){
        return shippingService.delete(shippingId);
    }

    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable("shippingId") Integer shippingId,
                             @Valid @RequestBody ShippingUpdateForm shippingUpdateForm){
        return shippingService.update(shippingId,shippingUpdateForm);
    }
    @GetMapping("/shippings")
    public ResponseVo get(@RequestBody(required = false) ShippingGetForm shippingGetForm,
                          HttpSession session){
        User user=(User)session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.get(user.getId(),shippingGetForm);
    }

}
