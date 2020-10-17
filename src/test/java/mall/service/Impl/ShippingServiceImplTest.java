package mall.service.Impl;

import mall.MallApplicationTests;
import mall.form.ShippingGetForm;
import mall.form.ShippingUpdateForm;
import com.github.pagehelper.PageInfo;
import mall.form.ShippingAddForm;
import mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class ShippingServiceImplTest extends MallApplicationTests {

    @Autowired
    ShippingServiceImpl shippingService;
    @Test
    public void add() {
        ShippingAddForm shippingAddForm=new ShippingAddForm();
        shippingAddForm.setReceiverMobile("138000000000");
        shippingAddForm.setReceiverName("哥");
        shippingAddForm.setReceiverPhone("+86");
        shippingAddForm.setReceiverProvince("北京");
        shippingAddForm.setReceiverZip("100000");
        shippingAddForm.setReceiverCity("北京市");
        shippingAddForm.setReceiverDistrict("北京区");
        shippingAddForm.setReceiverAddress("北京村");
        ResponseVo<Map<String, Integer>> add = shippingService.add(20, shippingAddForm);
        log.info("add={}{}",add.getStatus(),add.getData());
    }

    @Test
    public void delete() {
        ResponseVo<String> delete = shippingService.delete(7);
        log.info("delete={}{}",delete.getStatus(),delete.getData());
    }
    @Test
    public void update() {
        ShippingUpdateForm shippingUpdateForm=new ShippingUpdateForm();
        shippingUpdateForm.setReceiverMobile("138000000000");
        shippingUpdateForm.setReceiverName("小哥");
        shippingUpdateForm.setReceiverPhone("+86");
        shippingUpdateForm.setReceiverProvince("北京");
        shippingUpdateForm.setReceiverZip("100000");
        shippingUpdateForm.setReceiverCity("北京市");
        shippingUpdateForm.setReceiverDistrict("北京区");
        shippingUpdateForm.setReceiverAddress("北京村");
        ResponseVo<String> update = shippingService.update(8, shippingUpdateForm);
        log.info("update={}{}",update.getStatus(),update.getData());
    }
    @Test
    public void get() {
        ShippingGetForm shippingGetForm=new ShippingGetForm();
        ResponseVo<PageInfo> pageInfo = shippingService.get(20, shippingGetForm);
        log.info("pageInfo={}",pageInfo);
    }
}