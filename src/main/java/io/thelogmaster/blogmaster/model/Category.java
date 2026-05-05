package io.thelogmaster.blogmaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Category {
    // 주키
    // 카테고리 이름
    @Setter(AccessLevel.NONE)
    private int id;


    private String categoryName;

    @JsonIgnore // JSON 변환시 필드는 무시 (순환 참조 방지)
    public Map<Integer, Post> postMap;
}