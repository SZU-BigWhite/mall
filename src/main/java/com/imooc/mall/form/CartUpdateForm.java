package com.imooc.mall.form;

import lombok.Data;

@Data
public class CartUpdateForm {
    Integer quantity; //非必填
    Boolean selected;//非必填
}
