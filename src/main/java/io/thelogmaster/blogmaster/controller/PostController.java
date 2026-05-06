package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Post;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import io.thelogmaster.blogmaster.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PostController {

    // git 올릴 때 삭제요망
    private final CommentService commentService;
    private final MemoryRepository memoryRepository;


    @GetMapping("/posts/{postId}")
    public String detail(@PathVariable int postId, Model model) {

        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        model.addAttribute("post", post);
        model.addAttribute("postId", postId);
        model.addAttribute("comments", commentService.getCommentsByPost(postId));

        return "post/detail";
    }
}
