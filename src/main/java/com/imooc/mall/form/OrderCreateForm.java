package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderCreateForm {
    @NotNull
    Integer shippingId;
}
