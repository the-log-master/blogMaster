package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Category;
import io.thelogmaster.blogmaster.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : io.thelogmaster.blogmaster.controller
 * fileName       : CategoryController
 * author         : Admin
 * date           : 26. 5. 4.
 * description    : 카테고리 전용 컨트롤러
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 26. 5. 4.        Admin       최초 생성
 */
@Controller
public class CategoryController {

    private final CategoryService categoryService;
    //생성자
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //메뉴 - 카테고리
    @GetMapping("/api/categories")
    @ResponseBody //JSON데이터 응답
    public List<Category> getCategoriesApi(Model model) {

        return categoryService.getCategoryList();
    }


    //일괄 적용
    @PostMapping("/category/update-all")
    public ResponseEntity<String> updateAllCategories(
            @RequestParam(value = "ids", required = false) List<Integer> ids,
            @RequestParam(value = "categoryNames", required = false) List<String> names) {

        System.out.println("ids:"+ids);
        System.out.println("names:"+names);

        // 데이터가 넘어왔을 경우에만 서비스 로직 실행
       /* if (ids != null && names != null) {
            categoryService.syncCategories(ids, names);
            return ResponseEntity.ok("success"); // 자바스크립트가 이걸 받고 새로고침(location.reload()) 하게 함
        }*/

        //전부 삭제될 수도있어서 바꿈
        List<Integer> safeIds = (ids == null) ? new ArrayList<>() : ids;
        List<String> safeNames = (names == null) ? new ArrayList<>() : names;
        categoryService.syncCategories(safeIds, safeNames);

        return ResponseEntity.ok("success");

        //return ResponseEntity.badRequest().body("fail");
    }

}
