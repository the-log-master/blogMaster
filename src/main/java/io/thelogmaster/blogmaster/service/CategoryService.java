package io.thelogmaster.blogmaster.service;

import io.thelogmaster.blogmaster.model.Category;
import io.thelogmaster.blogmaster.model.Comment;
import io.thelogmaster.blogmaster.model.Post;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    /**
     * 전체 카테고리 리스트 반환
     */
    public List<Category> getCategoryList() {
        List<Category> categories = new ArrayList<>(MemoryRepository.categoryMap.values());

        for(Category category : categories){
            //카테고리에 속한 post의  수
            int postCount = category.getPostMap().size();
            //System.out.println("postCount:"+postCount);
        }

        return new ArrayList<>(MemoryRepository.categoryMap.values());

    }


    //한번에 저장용
    public void syncCategories(List<Integer> ids, List<String> names) {

        Map<Integer, Category> categoryMap = MemoryRepository.categoryMap;
        Category nonTitledCategory = categoryMap.get(0); //미분류 카테고리

        //삭제
        List<Integer> currentKeys = new ArrayList<>(categoryMap.keySet());
        for (Integer id : currentKeys) {
            // 0번은 지우지 않음 && 화면에서 넘어온 ids 명단에 이 ID가 없는거 확인
            if (id != 0 && !ids.contains(id)) {
                Category target = categoryMap.get(id);

                if (target != null) {
                    // 삭제될 카테고리의 글들을 미분류(0번)로 이동
                    if (target.getPostMap() != null && !target.getPostMap().isEmpty()) {
                        nonTitledCategory.getPostMap().putAll(target.getPostMap());

                        // 각 포스트 내부의 카테고리 정보도 0번으로 변경
                        target.getPostMap().values().forEach(post -> {
                            post.getCategoryMap().remove(id);
                            post.getCategoryMap().put(0, nonTitledCategory);
                        });
                    }
                    // 메모리에서 실제 삭제
                    categoryMap.remove(id);
                }
            }
        }


        //수정 및 추가
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            String name = names.get(i);

            if (id == -1) {
                // 신규 추가
                int newId = generateNextId();
                categoryMap.put(newId, new Category(newId, name, new HashMap<>()));
            } else if (id != 0 && categoryMap.containsKey(id)) {
                //0은 미분류라서 그외것을 처리함
                categoryMap.get(id).setCategoryName(name);
            }
        }

    }

    // 다음 ID 번호를 생성
    private int generateNextId() {
        return MemoryRepository.categoryMap.keySet().stream()
                .max(Integer::compare)
                .orElse(0) + 1;
    }
}
