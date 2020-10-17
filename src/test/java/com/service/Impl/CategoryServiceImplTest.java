package com.service.Impl;

import com.MallApplicationTests;
import mall.service.Impl.CategoryServiceImpl;
import mall.vo.CategoryVo;
import mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class CategoryServiceImplTest extends MallApplicationTests {
    
    @Autowired
    CategoryServiceImpl categoryService;
    @Test
    public void selectAll() {
        ResponseVo<List<CategoryVo>> listResponseVo = categoryService.selectAll();
        List<CategoryVo> data = listResponseVo.getData();
        log.info("data={}",data);
    }
    @Test
    public void findSubCategoryId(){
        Set<Integer> resultSet=new HashSet<>();
        categoryService.findSubCategoryId(100001,resultSet);
        log.info("resultSet={}",resultSet);
    }
}