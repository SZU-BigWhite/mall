package com.imooc.mall.controller;

import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.service.Impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @Autowired
    CategoryServiceImpl categoryService;
    @GetMapping("/categories")
    public ResponseVo selectAll(){
        return categoryService.selectAll();
    }
}
