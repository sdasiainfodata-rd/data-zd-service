package com.asiainfo.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class UrlController {
//    @RequestMapping("{method}")
//    private String goToUrl(@PathVariable("method") String method){
//        return method;
//    }

    @RequestMapping("/refuse")
    private ResponseEntity toRefuse(){
        HashMap<String, HttpStatus> status = new HashMap<>();
        status.put("status", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity(status, HttpStatus.UNAUTHORIZED);
    }
}