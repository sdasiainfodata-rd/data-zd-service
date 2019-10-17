package com.asiainfo.security.controller;

import com.asiainfo.security.entity.UserDP;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import com.asiainfo.security.service.UserMongoService;
import com.asiainfo.security.service.impl.UserMongoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mr.LkZ
 * @version 2019/10/1213:56
 */
@RestController
//@RequestMapping("api")
public class UserMongoController {
    @Autowired
    private UserMongoService userMongoService;

    @GetMapping("/user/{username}")
    public ResponseEntity getUserDp(@PathVariable("username")String username){
        return new ResponseEntity(userMongoService.findUserDpByName(username),HttpStatus.OK);
    }

//    ("查询用户")
    @GetMapping(value = "/users")
    public ResponseEntity getUsers(UserMongoCriteria criteria, Pageable pageable){
        return new ResponseEntity(null,HttpStatus.OK);
    }

//    ("新增用户")
    @PostMapping(value = "/users")
    public ResponseEntity create(@RequestBody UserDP resources){
        return new ResponseEntity(userMongoService.create(resources),HttpStatus.CREATED);
    }

//    ("修改用户")
    @PutMapping(value = "/users")
    public ResponseEntity update(@RequestBody UserDP resources){
        userMongoService.update(resources);
        return new ResponseEntity(HttpStatus.OK);
    }

//    ("删除用户")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity delete(@PathVariable String id){
        userMongoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }








}
