package com.imooc.mall.service.Impl;

import com.imooc.mall.enums.ProdustEnum;
import com.imooc.mall.vo.ResponseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.service.IProductService;
import com.imooc.mall.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imooc.mall.enums.ResponseEnum.OUT_SHOP_OR_DELETE;

@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    ProductMapper productMapper;
    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {

        Set<Integer> categoryIdSets=new HashSet<>();
        if(categoryId!=null){
            categoryService.findSubCategoryId(categoryId,categoryIdSets);
            categoryIdSets.add(categoryId);
        }

        //TODO 商品分页
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectCategoryIdSets(categoryIdSets);
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                }).collect(Collectors.toList());

        PageInfo pageInfo=new PageInfo<>(productList);
        pageInfo.setList(productVoList);
        return ResponseVo.success(pageInfo);
    }
    public ResponseVo<Product> productDetail(Integer productId){
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product.getStatus().equals(ProdustEnum.OUT_SHOP.getCode())||product.getStatus().equals(ProdustEnum.ON_DELETE.getCode()))
            return ResponseVo.error(OUT_SHOP_OR_DELETE);
        //对敏感数据处理
        product.setStock(product.getStock()>100?100:product.getStock());
        return ResponseVo.success(product);
    }
}
