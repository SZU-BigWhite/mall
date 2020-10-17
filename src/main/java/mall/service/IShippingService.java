package mall.service;

import mall.form.ShippingAddForm;
import mall.form.ShippingGetForm;
import mall.form.ShippingUpdateForm;
import com.github.pagehelper.PageInfo;
import mall.vo.ResponseVo;

import java.util.Map;

public interface IShippingService {
    ResponseVo<Map<String,Integer>> add(Integer userId, ShippingAddForm shippingAddForm);
    ResponseVo<String> delete(Integer shippingId);
    ResponseVo<String> update(Integer shippingId, ShippingUpdateForm shippingUpdateForm);
    ResponseVo<PageInfo> get(Integer userId, ShippingGetForm shippingGetForm);
}
