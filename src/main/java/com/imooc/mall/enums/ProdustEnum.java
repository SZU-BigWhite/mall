package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ProdustEnum {
    ON_SALE(1,"在售"),
    OUT_SHOP(2,"下架"),
    ON_DELETE(3,"删除"),
    ;
    Integer code;
    String detail;

    ProdustEnum(Integer code, String detail) {
        this.code = code;
        this.detail = detail;
    }
}
