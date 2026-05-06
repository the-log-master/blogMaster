package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.thelogmaster.blogmaster.service.PostListService;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import java.util.List;

@Controller
public class PostListController {
    @GetMapping(path = "/")
    public String showPostList(
            @RequestParam(defaultValue = "-1") int category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "false") boolean isPrivate,
            Model model) {

        List<Integer> postIdList =
                category < 0
                        ? (
                        isPrivate
                        ? MemoryRepository.entirePrivatePostIdList
                        : MemoryRepository.entirePublicPostIdList
                )

                        : (
                        isPrivate
                        ? MemoryRepository.categoryPostIdList.get(category)
                        : MemoryRepository.categoryPostIdList.get(category * 10000)
                );

        List<Post> postList = PostListService.queryPostList(
                postIdList,
                MemoryRepository.postCategoryMap,
                MemoryRepository.categoryMap,
                page
        );

        model.addAttribute(postList);

        return "index";
    }


}
