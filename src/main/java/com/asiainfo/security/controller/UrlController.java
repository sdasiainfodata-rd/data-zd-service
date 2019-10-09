package com.asiainfo.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UrlController {
    @RequestMapping("{method}")
    private String goToUrl(@PathVariable("method") String method){
        return method;
    }
}