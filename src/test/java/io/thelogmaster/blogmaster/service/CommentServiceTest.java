package io.thelogmaster.blogmaster.service;

import io.thelogmaster.blogmaster.model.Category;
import io.thelogmaster.blogmaster.model.Comment;
import io.thelogmaster.blogmaster.model.Post;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

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
    void 특정_게시글의_댓글을_조회한다() {
        // given
        int postId = 100;
        commentService.addComment(postId, "첫 번째 댓글");
        commentService.addComment(postId, "두 번째 댓글");

        // when
        List<Comment> comments = commentService.getCommentsByPost(postId);

        // then
        assertThat(comments).hasSize(2);
        assertThat(comments)
                .extracting(Comment::getContent)
                .containsExactlyInAnyOrder("첫 번째 댓글", "두 번째 댓글");
    }

    @Test
    void 댓글을_등록한다() {
        // given
        int postId = 100;
        String content = "등록 테스트 댓글";

        // when
        commentService.addComment(postId, content);

        // then
        List<Comment> comments = commentService.getCommentsByPost(postId);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getId()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo(content);
        assertThat(MemoryRepository.comments).hasSize(1);
    }

    @Test
    void 댓글을_수정한다() {
        // given
        int postId = 100;
        commentService.addComment(postId, "수정 전 댓글");

        Comment comment = commentService.getCommentsByPost(postId).get(0);

        // when
        commentService.updateComment(postId, comment.getId(), "수정 후 댓글");

        // then
        Comment updatedComment = commentService.getCommentsByPost(postId).get(0);

        assertThat(updatedComment.getContent()).isEqualTo("수정 후 댓글");
        assertThat(updatedComment.getUpdateAt()).isNotNull();
    }

    @Test
    void 댓글을_삭제한다() {
        // given
        int postId = 100;
        commentService.addComment(postId, "삭제할 댓글");

        Comment comment = commentService.getCommentsByPost(postId).get(0);

        // when
        commentService.removeComment(postId, comment.getId());

        // then
        assertThat(commentService.getCommentsByPost(postId)).isEmpty();
        assertThat(MemoryRepository.comments).isEmpty();
    }
}