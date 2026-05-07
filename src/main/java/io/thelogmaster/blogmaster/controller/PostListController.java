package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.thelogmaster.blogmaster.service.PostListService;
import io.thelogmaster.blogmaster.repository.MemoryRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostListController {
    private final PostListService postListService;

    public PostListController(PostListService postListService) {
        this.postListService = postListService;
    }

    @GetMapping(path = "/posts")
    public String showPostList(
            @RequestParam(defaultValue = "-1") int category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "true") boolean isOpen,
            Model model) {

        List<Integer> postIdList =
                category < 0
                        ? (
                        isOpen
                        ? MemoryRepository.entirePublicPostIdList
                        : MemoryRepository.entirePrivatePostIdList
                )

                        : (
                        isOpen
                        ? MemoryRepository.categoryPostIdList.get(category)
                        : MemoryRepository.categoryPostIdList.get(category + 10000)
                );

        List<Post> postList = PostListService.queryPostList(
                postIdList,
                MemoryRepository.postCategoryMap,
                MemoryRepository.categoryMap,
                page
        );

        int nextPage = PostListService.getNextPage(postIdList, page);
        int prevPage = PostListService.getPrevPage(postIdList, page);

        //model.addAttribute(postList);
        model.addAttribute("postList", postList);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("categoryId", category);

        return "post/list";
    }


}
