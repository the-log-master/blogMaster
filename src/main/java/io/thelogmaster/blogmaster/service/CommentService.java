package io.thelogmaster.blogmaster.service;

import io.thelogmaster.blogmaster.model.Comment;
import io.thelogmaster.blogmaster.model.Post;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private static final int ROOT_PARENT_ID = -1;

    private final MemoryRepository memoryRepository;

    // 특정 게시물에 달린 댓글 목록을 조회하는 메서드
    public List<Comment> getCommentsByPost(int postId) {
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        List<Comment> result = new ArrayList<>();

        List<Comment> rootComments = post.getCommentMap()
                .values()
                .stream()
                .filter(comment -> comment.getParentId() == ROOT_PARENT_ID)
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .toList();

        //컨트롤러를 통해 post를 리디이렉트 시 comment를 구조에 맞게 삽입하는 기능 수행
        for (Comment root : rootComments) {

            result.add(root);

            addChildren(
                    root.getId(),
                    post.getCommentMap().values(),
                    result
            );
        }

        return result;
    }

    // 게시글에 새로운 일반 댓글을 추가하는 메서드
    public void addComment(int postId, String content) {
        addCommentInternal(postId, content, ROOT_PARENT_ID, 0);
    }

    // 특정 댓글에 대댓글을 추가하는 메서드
    public void addReply(int postId, int parentId, String content) {
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Comment parent = post.getCommentMap().get(parentId);

        if (parent == null) {
            throw new IllegalArgumentException("부모 댓글 없음");
        }

        int depth = parent.getDepth() + 1;

        addCommentInternal(postId, content, parentId, depth);

    }

    // 댓글과 대댓글을 실제로 생성하고 저장하는 내부 공통 메서드
    private void addCommentInternal(
            int postId,
            String content,
            int parentId,
            int depth
    ) {
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        int commentId = memoryRepository.nextCommentId();

        if (commentId == parentId) {
            throw new IllegalStateException("댓글 id와 parentId가 같습니다.");
        }

        LocalDateTime now = LocalDateTime.now();

        Comment comment = new Comment(
                commentId,
                content,
                now,
                now,
                parentId,
                depth,
                new LinkedHashMap<>()
        );

        comment.setPost(post);

        post.getCommentMap().put(commentId, comment);
        MemoryRepository.comments.add(comment);
    }

    //기존의 댓글을 수정하는 메서드
    public void updateComment(int postId, int commentId, String content) {
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Comment comment = post.getCommentMap().get(commentId);

        if (comment == null) {
            throw new IllegalArgumentException("댓글 없음");
        }

        comment.setContent(content);
        comment.setUpdateAt(LocalDateTime.now());
    }

    //댓글을 삭제하는 메서드
    public void removeComment(int postId, int commentId) {
        Post post = memoryRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Set<Integer> deleteIds = new HashSet<>();
        collectChildCommentIds(post, commentId, deleteIds);

        post.getCommentMap().values().removeIf(comment ->
                deleteIds.contains(comment.getId())
        );

        MemoryRepository.comments.removeIf(comment ->
                deleteIds.contains(comment.getId())
        );
    }

    //특정 댓글 아래에 연결된 모든 자식 댓글 ID를 수집하는 메서드
    private void collectChildCommentIds(
            Post post,
            int commentId,
            Set<Integer> deleteIds
    ) {
        deleteIds.add(commentId);

        post.getCommentMap()
                .values()
                .stream()
                .filter(comment -> comment.getParentId() == commentId)
                .forEach(comment ->
                        collectChildCommentIds(post, comment.getId(), deleteIds)
                );
    }

    //부모 댓글 아래에 자식댓글을 붙여 댓글 계층구조를 만드는 메서드
    private void addChildren(
            int parentId,
            Collection<Comment> allComments,
            List<Comment> result
    ) {

        List<Comment> children = allComments.stream()
                .filter(comment -> comment.getParentId() == parentId)
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .toList();

        for (Comment child : children) {

            result.add(child);

            addChildren(
                    child.getId(),
                    allComments,
                    result
            );
        }
    }
}