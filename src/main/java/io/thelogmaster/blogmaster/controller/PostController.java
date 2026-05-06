package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Post;
import io.thelogmaster.blogmaster.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 목록 화면
// 팀원 2가 목록/페이징 만들기 전까지 임시 사용
   /* @GetMapping
    public String postList(Model model) {
        model.addAttribute("posts", postService.getPostList());

        return "post/list";
    }*///여기까지

    // 게시글 작성 폼
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("categories", postService.getCategoryList());

        return "post/write";
    }

    // 게시글 작성 처리
    @PostMapping("/write")
    public String savePost(
            @RequestParam(required = false) String userName,
            @RequestParam String postName,
            @RequestParam String content,
            @RequestParam(required = false) String isOpen,
            @RequestParam(required = false, defaultValue = "0") Integer categoryId
    ) {
        Boolean openValue = isOpen != null;

        postService.savePost(
                userName,
                openValue,
                postName,
                content,
                categoryId
        );

        return "redirect:/posts";
    }

    // 게시글 상세 조회 + 조회수 증가
    @GetMapping("/{postId}")
    public String postDetail(
            @PathVariable int postId,
            Model model
    ) {
        Optional<Post> postOptional = postService.getPostDetail(postId);

        if (postOptional.isEmpty()) {
            return "redirect:/posts";
        }

        Post post = postOptional.get();

        model.addAttribute("post", post);
        model.addAttribute("categoryId", postService.getCategoryIdByPostId(postId));
        model.addAttribute("categoryName", postService.getCategoryNameByPostId(postId));
        model.addAttribute("comments", post.getCommentMap().values());

        return "post/detail";
    }

    // 게시글 수정 폼
    @GetMapping("/{postId}/edit")
    public String editForm(
            @PathVariable int postId,
            Model model
    ) {
        Optional<Post> postOptional = postService.getPostForEdit(postId);

        if (postOptional.isEmpty()) {
            return "redirect:/posts";
        }

        model.addAttribute("post", postOptional.get());
        model.addAttribute("categories", postService.getCategoryList());
        model.addAttribute("selectedCategoryId", postService.getCategoryIdByPostId(postId));

        return "post/edit";
    }

    // 게시글 수정 처리
    @PostMapping("/{postId}/edit")
    public String updatePost(
            @PathVariable int postId,
            @RequestParam String postName,
            @RequestParam String content,
            @RequestParam(required = false) String isOpen,
            @RequestParam(required = false, defaultValue = "0") Integer categoryId
    ) {
        Boolean openValue = isOpen != null;

        boolean result = postService.updatePost(
                postId,
                openValue,
                postName,
                content,
                categoryId
        );

        if (!result) {
            return "redirect:/posts";
        }

        return "redirect:/posts/" + postId;
    }

    // 게시글 삭제 처리
    @PostMapping("/{postId}/delete")
    public String deletePost(
            @PathVariable int postId
    ) {
        postService.deletePost(postId);

        return "redirect:/posts";
    }
}