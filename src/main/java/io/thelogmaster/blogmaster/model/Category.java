package io.thelogmaster.blogmaster.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.Map;

@Data
@AllArgsConstructor
public class Category {
    // 주키
    // 카테고리 이름
    @Setter(AccessLevel.NONE)
    private int id;
    private String categoryName;

    public Map<Integer, Post> postMap;


}