package mall.service.Impl;

import mall.dao.CategoryMapper;
import mall.pojo.Category;
import mall.service.ICategoryService;
import mall.vo.CategoryVo;
import mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static mall.consts.MallConst.ROOT_PARENT_ID;

@Service
public class CategoryServiceImpl implements ICategoryService {
    /**
     * 耗时：http请求  >  磁盘（mysql内网+磁盘)  >  java内存
     */
    @Autowired
    private CategoryMapper categoryMapper;

    public ResponseVo<List<CategoryVo>> selectAll(){

        List<CategoryVo> categoryVoList=new ArrayList<>();
        List<Category> categories = categoryMapper.selectAll();

        for (Category category : categories) {
            if(category.getParentId().equals(ROOT_PARENT_ID)){
                categoryVoList.add(category2CategoryVoDao(category));
            }
        }

        categoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
        findSubCategories(categories,categoryVoList);



        return ResponseVo.success(categoryVoList);
    }
    public void findSubCategories(List<Category> categories,List<CategoryVo> categoryVoList){
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryVoList=new ArrayList<>();   //子目录
            //查到子目录
            for (Category category : categories) {
                if(categoryVo.getId().equals(category.getParentId())){
                    subCategoryVoList.add(category2CategoryVoDao(category));
                }
            }
            //子目录排序
            subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
            //加入子目录
            categoryVo.setSubCategories(subCategoryVoList);
            //子目录继续往下查
            findSubCategories(categories,subCategoryVoList);
        }
    }
    private CategoryVo category2CategoryVoDao(Category category){
        CategoryVo categoryVo=new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    public void findSubCategoryId(Integer id, Set<Integer> resultSet){
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id,resultSet,categories);
    }
    public void findSubCategoryId(Integer id, Set<Integer> resultSet,List<Category> categories) {
        for (Category category : categories) {
            if(category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }
    }

}
