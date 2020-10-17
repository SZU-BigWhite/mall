package mall.dao;

import mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    List<Shipping> selectByUserid(Integer userId);

    Shipping selectByIdAndUserId(Integer id,Integer userId);

    List<Shipping> selectByIdSet(@Param("idSet") Set<Integer> idSet);
}