package mall.controller;

import mall.service.Impl.CategoryServiceImpl;
import mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @Autowired
    CategoryServiceImpl categoryService;
    @GetMapping("/categories")
    public ResponseVo selectAll(){
        return categoryService.selectAll();
    }
}
