package com.asiainfo.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class DispatcherController {
    @RequestMapping("/refuse")
    private ResponseEntity toRefuse(){
        HashMap<String, Integer> status = new HashMap<>();
        status.put("status", 401);
        return new ResponseEntity(status, HttpStatus.UNAUTHORIZED);
    }
}