package mall.dao;

import mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectAll();

    List<Product> selectCategoryIdSets(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    List<Product> selectProductIdSets(@Param("productIdSet") Set<Integer> productIdSet);

}