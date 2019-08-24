package com.aaa.lee.shiro.controller;

import com.aaa.lee.shiro.model.Book;
import com.aaa.lee.shiro.service.BookService;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/8/5 10:37
 * @Description
 **/
@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/bookList")
    public String allBooks(ModelMap modelMap) {
        List<Book> bookList = new ArrayList<Book>();
        // 需要查询出所有的图书信息
        try {
            bookList = bookService.selectAllBooks();
            if(bookList.size() > 0) {
                modelMap.addAttribute("bookList", bookList);
                return "book_list";
            }
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            modelMap.addAttribute("msg", "没有角色！");
            return "404";
        }
        return "404";
    }
}
