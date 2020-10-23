package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum PayTypeEnum {
    PAY_ONLINE(1,"在线支付"),

    PAY_WHEN_GET(2,"货到付款"),

    ;
    Integer code;
    String desc;

    PayTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
