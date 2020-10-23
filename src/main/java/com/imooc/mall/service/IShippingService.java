package com.imooc.mall.service;

import com.imooc.mall.form.ShippingAddForm;
import com.imooc.mall.form.ShippingGetForm;
import com.imooc.mall.form.ShippingUpdateForm;
import com.imooc.mall.vo.ResponseVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface IShippingService {
    ResponseVo<Map<String,Integer>> add(Integer userId, ShippingAddForm shippingAddForm);
    ResponseVo<String> delete(Integer shippingId);
    ResponseVo<String> update(Integer shippingId, ShippingUpdateForm shippingUpdateForm);
    ResponseVo<PageInfo> get(Integer userId, ShippingGetForm shippingGetForm);
}
