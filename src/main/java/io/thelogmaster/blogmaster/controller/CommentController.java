package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Comment;
import io.thelogmaster.blogmaster.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
//
    @GetMapping("/posts/{postId}/comments")
    @ResponseBody
    public List<Comment> getCommentsByPost(
            @PathVariable int postId
    ) {
        return commentService.getCommentsByPost(postId);
    }

    @PostMapping("/posts/{postId}/comments/add")
    public String addComment(
            @PathVariable int postId,
            @RequestParam String content
    ) {

        commentService.addComment(postId, content);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/update")
    public String updateComment(
            @PathVariable int postId,
            @PathVariable int commentId,
            @RequestParam String content
    ) {
        commentService.updateComment(postId, commentId, content);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public String removeComment(
            @PathVariable int postId,
            @PathVariable int commentId
    ) {
        commentService.removeComment(postId, commentId);

        return "redirect:/posts/" + postId;
    }

    @GetMapping("/test/comment")
    public String testComment(Model model) {

        int postId = 0;

        model.addAttribute("postId", postId);
        model.addAttribute("comments", commentService.getCommentsByPost(postId));

        return "fragments/comment_section :: commentArea";
    }

}
