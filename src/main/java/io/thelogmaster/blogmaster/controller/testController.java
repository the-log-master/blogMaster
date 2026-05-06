package io.thelogmaster.blogmaster.controller;

import io.thelogmaster.blogmaster.model.Category;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Objects;

import static io.thelogmaster.blogmaster.repository.MemoryRepository.*;

@Controller
public class testController {
    @GetMapping(path = "/category")
    public void testPage1(HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "text/html;charset=UTF-8");

        PrintWriter writer = resp.getWriter();

        writer.println("카테고리 목록<br>");
        writer.println("================================<br>");
        writer.println("카테고리 이름<br>");
        writer.println("카테고리 아이디<br>");
        writer.println("카테고리 내 포스트 개수<br>");
        writer.println("================================<br>");

        categoryMap.forEach((_, category) -> {
            writer.println(category.getCategoryName());
            writer.println("<br>");
            writer.println(category.getId());
            writer.println("<br>");
            writer.println(category.postMap.size());
            writer.println("<br>");
            print(writer, "==============================");
        });
        writer.flush();
    }
//
//    @GetMapping(path = "/posts")
//    public void testPage2(
//            @RequestParam(defaultValue = "0") int id,
//            HttpServletRequest req,
//            HttpServletResponse resp
//    ) throws IOException {
//
//        resp.setHeader("Content-Type", "text/html;charset=UTF-8");
//
//        PrintWriter writer = resp.getWriter();
//
//        print(writer, "포스트 목록");
//        print(writer, "==============================");
//        print(writer, "포스트 아이디");
//        print(writer, "유저 이름");
//        print(writer, "카테고리명");
//        print(writer, "공개 여부");
//        print(writer, "포스트 이름");
//        print(writer, "포스트 내용");
//        print(writer, "작성 일자");
//        print(writer, "업데이트 일자");
//        print(writer, "댓글 갯수");
//        print(writer, "==============================");
//
//        //메모리상 미분류가 null로 처리되서 ...
//        id = changeIntegerNum(req.getParameter("id"));
//
//        categoryMap.get(id)
//                .postMap.forEach((_, post) -> {
//                    print(writer, String.valueOf(post.getId()));
//                    print(writer, post.getUserName());
//                    print(writer, String.valueOf(post.categoryMap.get("categoryName")));
//                    print(writer, String.valueOf(post.getIsOpen()));
//                    print(writer, post.getPostName());
//                    print(writer, post.getContent());
//                    print(writer, String.valueOf(post.getCreatedAt()));
//                    print(writer, String.valueOf(post.getUpdateAt()));
//                    print(writer, String.valueOf(post.commentMap.size()));
//                    print(writer, "==============================");
//                });
//        writer.flush();
//    }

    @GetMapping(path = "/comment")
    public void testPage3(
            @RequestParam(defaultValue = "0") int id_cat,
            @RequestParam(defaultValue = "0") int id_post,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        resp.setHeader("Content-Type", "text/html;charset=UTF-8");

        PrintWriter writer = resp.getWriter();

        print(writer, "댓글 목록");
        print(writer, "==============================");
        print(writer, "댓글 아이디");
        print(writer, "댓글 이름");
        print(writer, "작성 일자");
        print(writer, "업데이트 일자");
        print(writer, "==============================");


        //메모리상 미분류가 null로 처리되서 ...
        int idCat = changeIntegerNum(req.getParameter("id_cat"));
        int idPost = changeIntegerNum(req.getParameter("id_post"));

            categoryMap.get(idCat)
                .postMap.get(idPost)
                .getCommentMap()
                .forEach((_, comment) -> {
                    print(writer, String.valueOf(comment.getId()));
                    print(writer, comment.getContent());
                    print(writer, String.valueOf(comment.getUpdateAt()));
                    print(writer, String.valueOf(comment.getCreatedAt()));
                    print(writer, "==============================");
                });
        writer.flush();
    }

    void print(PrintWriter writer, String string) {
        writer.println(string + "<br>");
    }

    //index 확인용
    @GetMapping("/index")
    public String showIndexPage(){
        System.out.println("!!");
        return "index";
    }

    @GetMapping(path = "/post/all")
    public void allPosts(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();

        print(writer, "전체 포스트 목록");
        print(writer, "==============================");

        // 1. 모든 카테고리를 하나씩 꺼냅니다.
        categoryMap.forEach((catId, category) -> {

            // 2. 해당 카테고리에 포스트가 있는지 확인
            if (category.postMap != null && !category.postMap.isEmpty()) {

                // 3. 각 카테고리 내부의 postMap을 순회합니다.
                category.postMap.forEach((postId, post) -> {
                    print(writer, "카테고리 ID: " + catId);

                    // 카테고리 이름이 null이면 '미분류'로 표시
                    String catName = (category.getCategoryName() == null) ? "미분류" : category.getCategoryName();
                    print(writer, "카테고리명: " + catName);

                    print(writer, "포스트 아이디: " + post.getId());
                    print(writer, "유저 이름: " + post.getUserName());
                    print(writer, "포스트 이름: " + post.getPostName());
                    //print(writer, "포스트 내용: " + post.getContent());
                    print(writer, "작성 일자: " + post.getCreatedAt());
                    print(writer, "댓글 갯수: " + (post.commentMap != null ? post.commentMap.size() : 0));
                    print(writer, "------------------------------");
                });

            }
        });

        print(writer, "==============================");
        print(writer, "모든 포스트 출력 완료");
        writer.flush();
    }


    //null(미분류) 값으로 인한 0으로 처리하는 함수
    public int changeIntegerNum(String obj){

        String changeObj = (obj == null) ? "0" : obj;
        return Integer.parseInt(changeObj);

    }

}
