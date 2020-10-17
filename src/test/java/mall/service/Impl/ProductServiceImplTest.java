package mall.service.Impl;

import mall.MallApplicationTests;
import com.github.pagehelper.PageInfo;
import mall.pojo.Product;
import mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ProductServiceImplTest extends MallApplicationTests {

    @Autowired
    ProductServiceImpl productService;
    @Test
    public void list() {
        ResponseVo<PageInfo> list = productService.list(null, 2, 3);
        log.info("status={} data={}",list.getStatus(),list.getData());
    }
    @Test
    public void productDetail(){
        ResponseVo<Product> productResponseVo = productService.productDetail(26);
        log.info("status={} data={}",productResponseVo.getStatus(),productResponseVo.getData());
    }
}