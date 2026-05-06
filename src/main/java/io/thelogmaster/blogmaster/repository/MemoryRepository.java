package io.thelogmaster.blogmaster.repository;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import io.thelogmaster.blogmaster.model.Category;
import io.thelogmaster.blogmaster.model.Comment;
import io.thelogmaster.blogmaster.model.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Repository
public class MemoryRepository {
    public static Map<Integer, Category> categoryMap = new HashMap<>();
    // Comment 객체 저장소 생성
    public static final List<Comment> comments = new LinkedList<>();

    // 개발용 샘플 데이터 생성 로직, 서비스 완성시 제거할 것
    static Random random = new Random(
            LocalDateTime.now()
                    .toInstant(ZoneOffset.UTC)
                    .toEpochMilli());
    static Lorem lorem = LoremIpsum.getInstance();

    public static int categoryCount= random.nextInt(2, 4);
    public static int postCount = 1;
    public static int commentCount = 1;

    private static Comment genComment() {
        return new Comment(
                commentCount,
                lorem.getParagraphs(2, 5),
                LocalDateTime.now(),
                LocalDateTime.now(),
                new HashMap<>());
    }

    /**
     * 댓글 ID 생성
     */
    public int nextCommentId() {
        return commentCount++;
    }

    /**
     * postId로 Post 찾기
     */
    public Optional<Post> findPostById(int postId) {
        return categoryMap.values().stream()
                .flatMap(category -> category.getPostMap().values().stream())
                .filter(post -> post.getId() == postId)
                .findFirst();
    }


    private static Post genPost() {
        Map<Integer, Comment> commentMap = new HashMap<>();

        Post post = new Post(
                postCount,
                lorem.getName(),
                0,
                random.nextInt(2) == 0,
                lorem.getTitle(15),
                lorem.getParagraphs(10, 20),
                LocalDateTime.now(),
                LocalDateTime.now(),
                commentMap,
                new HashMap<>()
        );


        int n = random.nextInt(10);
        
        for (int i = 0; i < n; i += 1) {
            Comment comment = genComment();
            comment.getPostMap().put(postCount, post);
            commentMap.put(commentCount, comment);
            commentCount += 1;
        }
        
        return post;
    }

    private static Category genCategory(int id) {
        Map<Integer, Post> postMap = new HashMap<>();

        Category category = new Category(id, lorem.getName(), postMap);
        int n = random.nextInt(10);

        for (int i = 0; i < n; i += 1) {
            Post post = genPost();
            postMap.put(postCount, post);
            post.getCategoryMap().put(id, category);
            postCount += 1;
        }

        return category;
    }

    static {
        Map<Integer, Comment> h1 = new HashMap<>();
        Map<Integer, Post> h2 = new HashMap<>();
        Map<Integer, Category> h3 = new HashMap<>();

        Comment hangulComment = new Comment(
                0,
                "한글 댓글 내용",
                LocalDateTime.now(),
                LocalDateTime.now(),
                h2
        );

        Post hangulPost = new Post(
                0,
                "한글 작성자",
                0,
                true,
                "한글 제목",
                "추억도 사막으로 해저물었습니다. 놓인 멀리 위에도 추억도 영변에 그대 나와 너무나 좋은 때 인생은 네가 더 프랑시스 회한도 유유히 다 해저물었습니다. 아기 부서지는 노래, 나의 였다. <br>" +
                        "강 내일 그대에게 위에 젖가슴에 없어지고 하늘이여 망각의 불러 갈라놓는 무덤 시각에 너는 위에 있다 목란배 비둘기, 일편단심 비와 남서풍이 때에는 꽃잎을 오, 아름다운 햇빛 다.<br>" +
                        "유년의 하나둘 우는 건너온 보네 싶은 비로소 동경과 위에 임 걸음 노래, 그 보네 해저물었습니다. 그리도 유유히 인생은 날에 바위틈에 잊지 나를 가네 가시는 함께 됩니다.",
                LocalDateTime.now(),
                LocalDateTime.now(),
                h1,
                h3
        );

        Category nonTitledCategory = new Category(0, null, h2);

        h1.put(0, hangulComment);
        h2.put(0, hangulPost);
        h3.put(0, nonTitledCategory);

        categoryMap.put(0, nonTitledCategory);
        
       for (int i = 1; i < categoryCount; i += 1) {
           categoryMap.put(i, genCategory(i));
       }
    }
}


