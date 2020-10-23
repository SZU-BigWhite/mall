package com.imooc.mall.service;


import com.imooc.mall.vo.ResponseVo;

public interface ICouponService {
    ResponseVo sendCoupon(Integer productId, int num, int times, long amount);
    ResponseVo getCoupon(Integer productId,int userId);
}
