package com.asiainfo.webshow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Mr.LkZ
 * @version 2019/10/1417:02
 */
//@Controller
public class UrlController {
    @RequestMapping("/web/{html}")
    public String toHtml(@PathVariable("html") String html){
        return "web/"+html;
    }
}
