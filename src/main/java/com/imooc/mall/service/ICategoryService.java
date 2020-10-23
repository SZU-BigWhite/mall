package com.imooc.mall.service;

import com.imooc.mall.vo.ResponseVo;
import com.imooc.mall.vo.CategoryVo;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    ResponseVo<List<CategoryVo>> selectAll();
    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
