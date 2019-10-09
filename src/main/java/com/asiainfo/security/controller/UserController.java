package com.asiainfo.security.controller;

import com.asiainfo.security.entity.User;
import com.asiainfo.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Mr.LkZ
 * @version 2019/9/302:27
 */

@RestController
//@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return org.springframework.http.ResponseEntity
     */
    @RequestMapping("login")
    public ResponseEntity getLogin(String username, String password){
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password))return null;
        User user = userService.userLogin(username, password);
//        System.out.println(map);
        //设置token
        if (user==null||user.getUser()==null||user.getUser().size()==0) return null;
        HashMap map = user.getUser();
        Object id = map.get("_id");
        user.setToken(id.toString());
        return new ResponseEntity(user,HttpStatus.OK);
    }

    /**
     * 注册用户
     * @param hashMap 用户的参数
     */
    @RequestMapping("register")
    public void registerOneUser(@RequestParam HashMap<String,Object> hashMap){
        if (hashMap==null||hashMap.size()==0)return;
        if (hashMap.get("username")==null||hashMap.get("password")==null)return;
        hashMap.put("enabled", true);
        userService.insertOneUser(hashMap);
    }

    /**
     * 通过用户的id返回菜单实体类列表
     * @param userId 用户的id
     * @return org.springframework.http.ResponseEntity
     */
    @RequestMapping("menu/{userId}")
    public ResponseEntity getMenu(@PathVariable("userId") String userId){
        Set<String> menusById = userService.findMenusById(userId);
        return new ResponseEntity(userService.findMenuByPerms(menusById), HttpStatus.OK);
    }

    /**
     * 查询所有用户
     * @return org.springframework.http.ResponseEntity
     */
    @RequestMapping("users")
    public ResponseEntity getAllUsers(){
        return new ResponseEntity(userService.findAll(),HttpStatus.OK);
    }
}
