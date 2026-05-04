package io.thelogmaster.blogmaster.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Data
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
}
