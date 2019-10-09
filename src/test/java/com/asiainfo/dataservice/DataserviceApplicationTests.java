package com.asiainfo.dataservice;

import com.asiainfo.security.entity.User;
import com.asiainfo.security.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataserviceApplicationTests {
    @Autowired
    private UserService userService;

    @Test
    public void testUser(){
        HashMap tom = userService.findUserByName("tom");
        List api_permitions = (List) tom.get("api_permitions");
        for (Object api_permition : api_permitions) {
            System.out.println(api_permition.toString());
        }
        System.out.println(tom);
        System.out.println("=================================================");

        Object id = tom.get("_id");
        String s = id.toString();
        Set<String> urls = userService.findUrlsById(s);
        for (String url : urls) {
            System.out.println(url);
        }
        System.out.println("=======================================");
        Set<String> urlsById = userService.findUrlsById("5d9c75fc0dd2c2ea602aa893");
        System.out.println(urlsById);
        HashMap xiaokong = userService.findUserByName("xiaokong");
        System.out.println(xiaokong);
//        HashMap<String, Object> xiaokong1 = userService.userLogin("xiaokong", "123");
//        System.out.println(xiaokong1);
    }

    @Test
    public void testUser1(){
        HashMap tom = userService.findUserByName("bajie");
        System.out.println(tom);
        Object roles = tom.get("roles");
        System.out.println(roles);

        Object id = tom.get("_id");
        String s = id.toString();
        Set<String> urlsById = userService.findUrlsById(s);
        for (String s1 : urlsById) {
            System.out.println(s1);
        }

        User map = userService.userLogin("bajie", "123");
        System.out.println(map);
    }
}
