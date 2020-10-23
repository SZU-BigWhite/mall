package com.imooc.mall.service.Impl;

import com.imooc.mall.form.ShippingGetForm;
import com.imooc.mall.form.ShippingUpdateForm;
import com.imooc.mall.vo.ResponseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.ShippingAddForm;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.service.IShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ResponseVo<Map<String, Integer>> add(Integer userId, ShippingAddForm shippingAddForm) {
        Shipping shipping=new Shipping();
        BeanUtils.copyProperties(shippingAddForm,shipping);
        shipping.setUserId(userId);
        int row = shippingMapper.insertSelective(shipping);
        if(row==0)
            return ResponseVo.error(ResponseEnum.ERROR);
        Map<String, Integer> shippingId=new HashMap<>();
        shippingId.put("shippingId",shipping.getId());
        return ResponseVo.success(shippingId);
    }

    @Override
    public ResponseVo<String> delete(Integer shippingId) {
        int row = shippingMapper.deleteByPrimaryKey(shippingId);
        if(row==0)
            return ResponseVo.error(ResponseEnum.SHIPPING_EMPTY);
        return ResponseVo.success("删除地址成功");
    }

    @Override
    public ResponseVo<String> update(Integer shippingId, ShippingUpdateForm shippingUpdateForm) {
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping==null)
            return ResponseVo.error(ResponseEnum.SHIPPING_EMPTY);
        BeanUtils.copyProperties(shippingUpdateForm,shipping);
        shippingMapper.updateByPrimaryKeySelective(shipping);
        return ResponseVo.success("更新地址成功");
    }

    @Override
    public ResponseVo<PageInfo> get(Integer userId, ShippingGetForm shippingGetForm) {
        PageHelper.startPage(shippingGetForm.getPageNum(),shippingGetForm.getPageSize());
        List<Shipping> shippings = shippingMapper.selectByUserid(userId);
        PageInfo pageInfo=new PageInfo(shippings);
        return ResponseVo.success(pageInfo);
    }
}
