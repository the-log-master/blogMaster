package io.thelogmaster.blogmaster.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Comment {
    // 주키
    // 댓글 내용
    // 작성 일자
    // 최종 수정 일자

    @Setter(AccessLevel.NONE)
    private int id;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    // -1이면 원댓글, 그 외에는 부모 댓글 id
    private int parentId;
    // 원댓글 0, 대댓글 1, 대대댓글 2 ...
    private int depth;

    public Map<Integer, Post> postMap;

    public void setPost(Post post) {
        if (this.postMap == null) {
            this.postMap = new LinkedHashMap<>();
        }

        this.postMap.put(post.getId(), post);
    }
}
