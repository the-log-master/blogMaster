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

    public static int getNextPage(List<Integer> postIdList, int page) {
        int maxPage = Integer.max(1, postIdList.size() / 10 + (postIdList.size() % 10 == 0 ? 0 : 1));
        return Integer.min(page + 1, maxPage);
    }

    public static int getPrevPage(List<Integer> postIdList, int page) {
        int maxPage = Integer.max(1, postIdList.size() / 10 + (postIdList.size() % 10 == 0 ? 0 : 1));
        if (page - 1 > maxPage) {
            return maxPage;
        }
        return Integer.max(page - 1, 1);
    }

    public static void deletePostListItem(
            Map<Integer, List<Integer>> categoryPostIdList,
            List<Integer> entirePublicPosIdtList,
            List<Integer> entirePrivatePostIdList,
            Map<Integer, Integer> postCategoryMap,
            int postId,
            boolean isOpen) {

        int categoryId = postCategoryMap.get(postId);
        List<Integer> postIdList = isOpen ? entirePublicPosIdtList : entirePrivatePostIdList;

        int idx = Collections.binarySearch(postIdList, postId);
        if (idx < 0) {
            idx = -(idx + 1);
        }
        postIdList.remove(idx);

        idx = Collections.binarySearch(categoryPostIdList.get(categoryId), postId);
        if (idx < 0) {
            idx = -(idx + 1);
        }
        categoryPostIdList.get(categoryId).remove(idx);
    }

    public static void addPostListItem(
            Map<Integer, List<Integer>> categoryPostIdList,
            List<Integer> entirePublicPosIdtList,
            List<Integer> entirePrivatePostIdList,
            Map<Integer, Integer> postCategoryMap,
            int postId,
            int categoryId,
            boolean isOpen) {

        List<Integer> postIdList = isOpen ? entirePublicPosIdtList : entirePrivatePostIdList;
        postCategoryMap.put(postId, categoryId);

        int idx = Collections.binarySearch(postIdList, postId);
        if (idx < 0) {
            idx = -(idx + 1);
        }
        postIdList.add(idx, postId);

        idx = Collections.binarySearch(categoryPostIdList.get(categoryId), postId);
        if (idx < 0) {
            idx = -(idx + 1);
        }
        categoryPostIdList.get(categoryId).add(idx, postId);
    }
 }
