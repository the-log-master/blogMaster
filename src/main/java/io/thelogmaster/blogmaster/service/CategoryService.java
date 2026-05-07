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
import java.util.stream.Collectors;

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

        return MemoryRepository.categoryMap
                .values()
                .stream()
                .filter(
                        category -> category.getId() < 10000)
                .collect(Collectors.toList());
    }


    //한번에 저장용
    public void syncCategories(List<Integer> ids, List<String> names) {

        Map<Integer, Category> categoryMap = MemoryRepository.categoryMap;
        Category nonTitledCategory = categoryMap.get(0); //미분류 카테고리=

        //삭제
        List<Integer> currentKeys = new ArrayList<>(categoryMap.keySet());
        for (Integer id : currentKeys) {
            // 0번은 지우지 않음 && 화면에서 넘어온 ids 명단에 이 ID가 없는거 확인
            if (id != 0 && !ids.contains(id) && id < 10000) {
                Category target = categoryMap.get(id);

                if (target != null) {
                    // 삭제될 카테고리의 글들을 미분류(0번)로 이동
                    if (target.getPostMap() != null && !target.getPostMap().isEmpty()) {
                        nonTitledCategory.getPostMap().putAll(target.getPostMap());

                        // 각 포스트 내부의 카테고리 정보도 0번으로 변경
                        // 카테고리 내 포스트 목록을 미분류로 옮기는 작업
                        target.getPostMap().values().forEach(post -> {
                            PostListService.deletePostListItem(
                                    MemoryRepository.categoryPostIdList,
                                    MemoryRepository.entirePublicPostIdList,
                                    MemoryRepository.entirePrivatePostIdList,
                                    MemoryRepository.postCategoryMap,
                                    post.getId(),
                                    true
                            );


                            post.getCategoryMap().remove(id);
                            post.getCategoryMap().put(0, nonTitledCategory);

                            PostListService.addPostListItem(
                                    MemoryRepository.categoryPostIdList,
                                    MemoryRepository.entirePublicPostIdList,
                                    MemoryRepository.entirePrivatePostIdList,
                                    MemoryRepository.postCategoryMap,
                                    post.getId(),
                                    0,
                                    post.getIsOpen()
                            );
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
                categoryMap.put(newId + 10000, new Category(newId + 10000, name, new HashMap<>()));

                MemoryRepository.categoryPostIdList.put(newId, new ArrayList<>());
                MemoryRepository.categoryPostIdList.put(newId + 10000, new ArrayList<>());

            } else if (id != 0 && categoryMap.containsKey(id)) {
                //0은 미분류라서 그외것을 처리함
                categoryMap.get(id).setCategoryName(name);
            }
        }

    }

    // 다음 ID 번호를 생성
    private int generateNextId() {
        return MemoryRepository.categoryCount++;
    }
}
