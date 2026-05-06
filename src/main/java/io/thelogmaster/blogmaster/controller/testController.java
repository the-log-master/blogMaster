package io.thelogmaster.blogmaster.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

    @GetMapping(path = "/post")
    public void testPage2 (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "text/html;charset=UTF-8");

        PrintWriter writer = resp.getWriter();

        print(writer, "포스트 목록");
        print(writer, "==============================");
        print(writer, "포스트 아이디");
        print(writer, "유저 이름");
        print(writer, "공개 여부");
        print(writer, "포스트 이름");
        print(writer, "포스트 내용");
        print(writer, "작성 일자");
        print(writer, "업데이트 일자");
        print(writer, "댓글 갯수");
        print(writer, "==============================");

        categoryMap.get(Integer.parseInt(req.getParameter("id")))
                .postMap.forEach((_, post) -> {
                    print(writer, String.valueOf(post.getId()));
                    print(writer, post.getUserName());
                    print(writer, String.valueOf(post.getIsOpen()));
                    print(writer, post.getPostName());
                    print(writer, post.getContent());
                    print(writer, String.valueOf(post.getCreatedAt()));
                    print(writer, String.valueOf(post.getUpdateAt()));
                    print(writer, String.valueOf(post.commentMap.size()));
                    print(writer, "==============================");
                });
        writer.flush();
    }


    @GetMapping(path = "/comment")
    public void testPage3 (HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "text/html;charset=UTF-8");

        PrintWriter writer = resp.getWriter();

        print(writer, "댓글 목록");
        print(writer, "==============================");
        print(writer, "댓글 아이디");
        print(writer, "댓글 이름");
        print(writer, "작성 일자");
        print(writer, "업데이트 일자");
        print(writer, "==============================");

        categoryMap.get(Integer.parseInt(req.getParameter("id_cat")))
                .postMap.get(Integer.parseInt(req.getParameter("id_post")))
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
}
