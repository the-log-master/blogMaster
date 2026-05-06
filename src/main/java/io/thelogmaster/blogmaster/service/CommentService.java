package io.thelogmaster.blogmaster.service;

import io.thelogmaster.blogmaster.model.Comment;
import io.thelogmaster.blogmaster.model.Post;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final MemoryRepository memoryRepository;

    public CommentService(MemoryRepository memoryRepository){
        this.memoryRepository = memoryRepository;
    }

//    post에서 제대로 가져올 수 있을지 확인 필요
    public List<Comment> getCommentsByPost(int postId) {
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. postId=" + postId));

        return post.getCommentMap()
                .values()
                .stream()
                .toList();
    }


    public void addComment(int postId, String content){
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. postId=" + postId));

        int commentId = memoryRepository.nextCommentId();
        LocalDateTime now = LocalDateTime.now();

        Comment comment = new Comment(
                commentId,
                content,
                now,
                now,
                new HashMap<>()
        );

        comment.setPost(post);

        post.getCommentMap().put(commentId, comment);
        MemoryRepository.comments.add(comment);
    }

    public void updateComment(int postId,int commentId, String content){
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. postId=" + postId));

        Comment comment = post.getCommentMap().get(commentId);

        if (comment == null) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다. commentId=" + commentId);
        }

        comment.setContent(content);
        comment.setUpdateAt(LocalDateTime.now());
    }

    public void removeComment(int postId, int commentId){
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. postId=" + postId));

        post.getCommentMap().remove(commentId);

        MemoryRepository.comments.removeIf(comment -> comment.getId() == commentId);
    }


}
