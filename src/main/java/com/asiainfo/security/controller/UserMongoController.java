package com.asiainfo.security.controller;

import com.asiainfo.security.service.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    public HashMap getUserDp(@PathVariable("username")String username){
        return userMongoService.findUserDpByName(username);
    }




}
