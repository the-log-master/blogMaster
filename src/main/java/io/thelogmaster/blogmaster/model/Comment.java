package io.thelogmaster.blogmaster.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
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

    public Map<Integer, Post> postMap;

    public void setPost(Post post) {
        if (this.postMap == null) {
            this.postMap = new HashMap<>();
        }

        this.postMap.put(post.getId(), post);
    }
}
