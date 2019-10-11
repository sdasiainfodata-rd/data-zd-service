package com.asiainfo.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UrlController {
//    @RequestMapping("{method}")
//    private String goToUrl(@PathVariable("method") String method){
//        return method;
//    }

    @RequestMapping("/refuse")
    private ResponseEntity toRefuse(){
        return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
    }
}