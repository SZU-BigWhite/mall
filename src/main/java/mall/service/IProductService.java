package mall.service;

import com.github.pagehelper.PageInfo;
import mall.pojo.Product;
import mall.vo.ResponseVo;

public interface IProductService {
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);
    ResponseVo<Product> productDetail(Integer productId);
}
