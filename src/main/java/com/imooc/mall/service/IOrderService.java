package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.vo.OrderVo;

public interface IOrderService {
    ResponseVo<OrderVo> create(Integer userId, Integer shippingId);
    ResponseVo<PageInfo> list(Integer userId, Integer pageSize, Integer pageNum);
    ResponseVo<OrderVo> detail(Integer userId,Long orderNo);
    ResponseVo cancel(Integer userId,Long orderNo);
    void paid(Long orderNo);
}
