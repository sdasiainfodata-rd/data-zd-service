package com.asiainfo.security.controller;

import com.asiainfo.security.entity.User;
import com.asiainfo.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.LkZ
 * @version 2019/9/302:27
 */

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return com.asiainfo.security.entity.User
     */
    @RequestMapping("login")
    public User getLogin(String username, String password){
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password))return null;
        User user = userService.userLogin(username, password);
//        System.out.println(map);
        //设置token
        if (user==null||user.getUser()==null||user.getUser().size()==0) return null;
        HashMap map = user.getUser();
        Object id = map.get("_id");
        user.setToken(id.toString());
        return user;
    }

    @RequestMapping("register")
    public void registerOneUser(@RequestParam HashMap<String,Object> hashMap){
        if (hashMap==null||hashMap.size()==0)return;
        if (hashMap.get("username")==null||hashMap.get("password")==null)return;
        hashMap.put("enabled", true);
        userService.insertOneUser(hashMap);
    }
}
