package io.thelogmaster.blogmaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    //추가
    // JSON 출력 시 "postCount"라는 이름으로 개수만 포함시킴
    @JsonProperty("postCount")
    public int getPostCount() {
        return postMap != null ? postMap.size() : 0;
    }

    @JsonIgnore // JSON 변환시 필드는 무시 (순환 참조 방지)
    public Map<Integer, Post> postMap;


}