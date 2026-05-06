package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Category;
import io.thelogmaster.blogmaster.model.Comment;
import io.thelogmaster.blogmaster.model.Post;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MemoryRepository.categoryMap.clear();
        MemoryRepository.comments.clear();
        MemoryRepository.commentCount = 1;

        HashMap<Integer, Comment> commentMap = new HashMap<>();
        HashMap<Integer, Post> postMap = new HashMap<>();
        HashMap<Integer, Category> categoryMap = new HashMap<>();

        Post post = new Post(
                100,
                "테스트 작성자",
                0,
                true,
                "테스트 제목",
                "테스트 본문",
                LocalDateTime.now(),
                LocalDateTime.now(),
                commentMap,
                categoryMap
        );

        Category category = new Category(
                10,
                "테스트 카테고리",
                postMap
        );

        postMap.put(post.getId(), post);
        categoryMap.put(category.getId(), category);

        MemoryRepository.categoryMap.put(category.getId(), category);
    }

    @Test
    void 댓글을_등록하면_게시글_상세페이지로_리다이렉트된다() throws Exception {
        mockMvc.perform(post("/posts/100/comments/add")
                        .param("content", "컨트롤러 댓글 등록 테스트"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/100"));
    }

    @Test
    void 특정_게시글의_댓글을_JSON으로_조회한다() throws Exception {
        mockMvc.perform(post("/posts/100/comments/add")
                .param("content", "조회 테스트 댓글"));

        mockMvc.perform(get("/posts/100/comments"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("조회 테스트 댓글")));
    }

    @Test
    void 댓글을_수정하면_게시글_상세페이지로_리다이렉트된다() throws Exception {
        mockMvc.perform(post("/posts/100/comments/add")
                .param("content", "수정 전 댓글"));

        int commentId = MemoryRepository.comments.get(0).getId();

        mockMvc.perform(post("/posts/100/comments/" + commentId + "/update")
                        .param("content", "수정 후 댓글"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/100"));
    }

    @Test
    void 댓글을_삭제하면_게시글_상세페이지로_리다이렉트된다() throws Exception {
        mockMvc.perform(post("/posts/100/comments/add")
                .param("content", "삭제할 댓글"));

        int commentId = MemoryRepository.comments.get(0).getId();

        mockMvc.perform(post("/posts/100/comments/" + commentId + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/100"));
    }
}
