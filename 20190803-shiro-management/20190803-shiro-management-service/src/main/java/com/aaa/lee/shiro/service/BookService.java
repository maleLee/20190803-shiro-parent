package com.aaa.lee.shiro.service;

import com.aaa.lee.shiro.mapper.BookMapper;
import com.aaa.lee.shiro.model.Book;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/8/5 10:39
 * @Description
 **/
@Service
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    @RequiresRoles({"book_manager"})
    public List<Book> selectAllBooks() throws UnauthorizedException {
        return bookMapper.selectAll();
    }

}
