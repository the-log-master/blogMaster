package io.thelogmaster.blogmaster.service;

import io.thelogmaster.blogmaster.model.Category;
import io.thelogmaster.blogmaster.model.Comment;
import io.thelogmaster.blogmaster.model.Post;
import io.thelogmaster.blogmaster.repository.MemoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.thelogmaster.blogmaster.service.PostListService.addPostListItem;
import static io.thelogmaster.blogmaster.service.PostListService.deletePostListItem;

@Service
public class PostService {

    // 작성/수정 폼에서 카테고리 선택할 때 사용
    public Collection<Category> getCategoryList() {
        return MemoryRepository.categoryMap.values();
    }

    // 전체 게시글 목록 조회
    // 임시 목록 화면용
    public Collection<Post> getPostList() {
        Map<Integer, Post> postMap = new HashMap<>();

        for (Category category : MemoryRepository.categoryMap.values()) {
            postMap.putAll(category.getPostMap());
        }

        return postMap.values();
    }
    //여기까지 임시

    // 게시글 상세 조회 + 조회수 증가
    public Optional<Post> getPostDetail(int postId) {
        Optional<Post> postOptional = findPostById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setViewCount(post.getViewCount() + 1);
        }

        return postOptional;
    }

    // 수정 폼에서 사용할 게시글 조회
    // 상세 조회와 다르게 조회수를 증가시키면 안 됨
    public Optional<Post> getPostForEdit(int postId) {
        return findPostById(postId);
    }

    // 게시글 작성
    public Post savePost(
            String userName,
            Boolean isOpen,
            String postName,
            String content,
            Integer categoryId
    ) {

        // 0~9999 공개 포스트 10000~19999 비공개 포스트
        int saveCategoryId = (categoryId == null ? 0 : categoryId) + 10000 * (isOpen ? 0 : 1);

        Category category = MemoryRepository.categoryMap.get(saveCategoryId);

        // 없는 카테고리 ID가 들어오면 "카테고리 없음"으로 저장
        if (category == null) {
            saveCategoryId = 10000 * (isOpen ? 0 : 1);
            category = MemoryRepository.categoryMap.get(saveCategoryId);
        }

        int newPostId = MemoryRepository.postCount;
        MemoryRepository.postCount += 1;

        LocalDateTime now = LocalDateTime.now();

        Map<Integer, Comment> commentMap = new HashMap<>();
        Map<Integer, Category> postCategoryMap = new HashMap<>();

        // 공개 포스트 비공개 포스트 목록 분류

        Post post = new Post(
                newPostId,
                userName == null || userName.isBlank() ? "익명 작성자" : userName,
                0,
                isOpen,
                postName,
                content,
                now,
                now,
                commentMap,
                postCategoryMap
        );

        postCategoryMap.put(category.getId(), category);
        category.getPostMap().put(newPostId, post);

        addPostListItem(MemoryRepository.categoryPostIdList,
                MemoryRepository.entirePublicPostIdList,
                MemoryRepository.entirePrivatePostIdList,
                MemoryRepository.postCategoryMap,
                newPostId,
                saveCategoryId,
                isOpen);

        return post;
    }

    // 게시글 수정
    public boolean updatePost(
            int postId,
            Boolean isOpen,
            String postName,
            String content,
            Integer categoryId
    ) {
        Optional<PostLocation> postLocationOptional = findPostLocationById(postId);

        if (postLocationOptional.isEmpty()) {
            return false;
        }

        PostLocation postLocation = postLocationOptional.get();
        Post post = postLocation.getPost();

        boolean oldIsOpen = post.getIsOpen();

        post.setIsOpen(isOpen);
        post.setPostName(postName);
        post.setContent(content);
        post.setUpdateAt(LocalDateTime.now());

        int newCategoryId = categoryId == null ? 0 : categoryId + 10000 * (isOpen ? 0 : 1);

        Category newCategory = MemoryRepository.categoryMap.get(newCategoryId);

        if (newCategory == null) {
            newCategory = MemoryRepository.categoryMap.get(10000 * (isOpen ? 0 : 1));
        }

        Category oldCategory = postLocation.getCategory();

        // 카테고리가 바뀌었으면 기존 카테고리 postMap에서 빼고 새 카테고리 postMap에 넣음
        if (oldCategory.getId() != newCategoryId) {
            // 기존 포스트 목록에서 제거
            deletePostListItem(MemoryRepository.categoryPostIdList,
                    MemoryRepository.entirePublicPostIdList,
                    MemoryRepository.entirePrivatePostIdList,
                    MemoryRepository.postCategoryMap,
                    postId,
                    oldIsOpen);

            oldCategory.getPostMap().remove(postId);
            newCategory.getPostMap().put(postId, post);

            post.getCategoryMap().clear();
            post.getCategoryMap().put(newCategory.getId(), newCategory);

            // 새로운 포스트 목록에 추가
            addPostListItem(MemoryRepository.categoryPostIdList,
                    MemoryRepository.entirePublicPostIdList,
                    MemoryRepository.entirePrivatePostIdList,
                    MemoryRepository.postCategoryMap,
                    postId,
                    newCategoryId,
                    isOpen);
        }

        return true;
    }

    // 게시글 삭제
    // Soft Delete가 아니라 실제로 Map에서 제거
    public boolean deletePost(int postId) {
        Optional<PostLocation> postLocationOptional = findPostLocationById(postId);

        if (postLocationOptional.isEmpty()) {
            return false;
        }

        PostLocation postLocation = postLocationOptional.get();
        postLocation.getCategory().getPostMap().remove(postId);

        deletePostListItem(
                MemoryRepository.categoryPostIdList,
                MemoryRepository.entirePublicPostIdList,
                MemoryRepository.entirePrivatePostIdList,
                MemoryRepository.postCategoryMap,
                postId,
                postLocation.post.getIsOpen()
        );

        return true;
    }

    // 게시글이 속한 카테고리 ID 조회
    public Integer getCategoryIdByPostId(int postId) {
        Optional<PostLocation> postLocationOptional = findPostLocationById(postId);

        if (postLocationOptional.isEmpty()) {
            return 0;
        }

        return postLocationOptional.get().getCategory().getId();
    }

    // 게시글이 속한 카테고리 이름 조회
    public String getCategoryNameByPostId(int postId) {
        Optional<PostLocation> postLocationOptional = findPostLocationById(postId);

        if (postLocationOptional.isEmpty()) {
            return "카테고리 없음";
        }

        Category category = postLocationOptional.get().getCategory();

        if (category.getCategoryName() == null || category.getCategoryName().isBlank()) {
            return "카테고리 없음";
        }

        return category.getCategoryName();
    }

    // 게시글 ID로 게시글만 찾기
    private Optional<Post> findPostById(int postId) {
        for (Category category : MemoryRepository.categoryMap.values()) {
            Post post = category.getPostMap().get(postId);

            if (post != null) {
                return Optional.of(post);
            }
        }

        return Optional.empty();
    }

    // 게시글 ID로 게시글 + 소속 카테고리까지 찾기
    // 수정, 삭제, 카테고리 이동에 필요함
    private Optional<PostLocation> findPostLocationById(int postId) {
        for (Category category : MemoryRepository.categoryMap.values()) {
            Post post = category.getPostMap().get(postId);

            if (post != null) {
                return Optional.of(new PostLocation(category, post));
            }
        }

        return Optional.empty();
    }

    private static class PostLocation {
        private final Category category;
        private final Post post;

        public PostLocation(Category category, Post post) {
            this.category = category;
            this.post = post;
        }

        public Category getCategory() {
            return category;
        }

        public Post getPost() {
            return post;
        }
    }
}