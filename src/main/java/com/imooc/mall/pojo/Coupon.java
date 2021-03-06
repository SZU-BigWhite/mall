package com.imooc.mall.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Coupon {
    private Integer id;

    private Integer productid;

    private BigDecimal decrease;

    private Integer num;

}