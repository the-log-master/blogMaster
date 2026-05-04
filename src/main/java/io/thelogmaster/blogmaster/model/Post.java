package io.thelogmaster.blogmaster.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class Post {
//    주키
//    유저 이름
//    조회수
//    공개여부
//    게시글 이름
//    게시글 내용
//    작성 일자
//    최종 수정 일자

    @Setter(AccessLevel.NONE)
    private int id;

    private String userName;
    private int viewCount;
    private Boolean isOpen;

    private String postName;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public Map<Integer, Comment> commentMap;
    public Map<Integer, Category> categoryMap;
}
