package io.thelogmaster.blogmaster.service;

import io.thelogmaster.blogmaster.model.Category;
import io.thelogmaster.blogmaster.model.Post;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostListService {
    public static List<Post> queryPostList(
            List<Integer> postIdList,
            Map<Integer, Integer> postCategoryMap,
            Map<Integer, Category> categoryMap,
            int idx
    ) {
        List<Post> postList = new ArrayList<>();

        for (int i = (idx - 1) * 10; i < Integer.min(idx * 10, postIdList.size()); i += 1) {
            int postId = postIdList.get(i);

            postList.add(
                    categoryMap.get(postCategoryMap.get(postId))
                            .getPostMap()
                            .get(postId)
            );
        }

        return postList;
    }

    public void deletePostListItem(
            Map<Integer, List<Integer>> categoryPostIdList,
            List<Integer> entirePosIdtList,
            Map<Integer, Integer> postCategoryMap,
            int postId) {

        int categoryId = postCategoryMap.get(postId);

        entirePosIdtList.remove(
                Collections.binarySearch(
                        entirePosIdtList,
                        postId
                )
        );
        entirePosIdtList.remove(
                Collections.binarySearch(
                        categoryPostIdList.get(categoryId),
                        postId
                )
        );
    }
 }
