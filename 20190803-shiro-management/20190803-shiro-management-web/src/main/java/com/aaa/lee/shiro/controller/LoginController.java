package com.aaa.lee.shiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/8/3 16:22
 * @Description
 **/
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public String index() {
        // 根据业务逻辑需要，判断代码！！！
        return "index";
    }
}
