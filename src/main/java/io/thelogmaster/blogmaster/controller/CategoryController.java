package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Category;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

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

    private Category category; //카테고리 dto
    private MemoryRepository memoryRepository; // 공통저장소

    @GetMapping("/category_list")
    public String categoryList(Model model, HttpServletRequest req){

        ArrayList<Category>categoryList = memoryRepository.genCategory();
       model.addAttribute("categoryList", );


        return "category/list";
    }


}
