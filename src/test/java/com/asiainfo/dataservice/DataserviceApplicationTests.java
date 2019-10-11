package com.asiainfo.dataservice;

import com.asiainfo.security.mapper.UserMapper;
import com.asiainfo.security.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataserviceApplicationTests {
//    @Autowired
//    private UserService userService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserMapper userMapper;

//    @Test
//    public void testUser(){
//        HashMap tom = userService.findUserByName("tom");
//        List api_permitions = (List) tom.get("apiPermitions");
//        for (Object api_permition : api_permitions) {
//            System.out.println(api_permition.toString());
//        }
//        System.out.println(tom);
//        System.out.println("=================================================");
//
//        Object id = tom.get("_id");
//        String s = id.toString();
//        Set<String> urls = userService.findUrlsById(s);
//        for (String url : urls) {
//            System.out.println(url);
//        }
//        System.out.println("=======================================");
//        Set<String> urlsById = userService.findUrlsById("5d9c75fc0dd2c2ea602aa893");
//        System.out.println(urlsById);
//        HashMap xiaokong = userService.findUserByName("xiaokong");
//        System.out.println(xiaokong);
////        HashMap<String, Object> xiaokong1 = userService.userLogin("xiaokong", "123");
////        System.out.println(xiaokong1);
//    }
//
//    @Test
//    public void testUser1(){
//        HashMap tom = userService.findUserByName("bajie");
//        System.out.println(tom);
//        Object roles = tom.get("roles");
//        System.out.println(roles);
//
//        Object id = tom.get("_id");
//        String s = id.toString();
//        Set<String> urlsById = userService.findUrlsById(s);
//        for (String s1 : urlsById) {
//            System.out.println(s1);
//        }
//
//        UserMG map = userService.userLogin("bajie", "123");
//        System.out.println(map);
//    }
//
//    @Test
//    public void testMenu(){
//        Query query = new Query();
//        List<MenuVo> menu = mongoTemplate.find(query, MenuVo.class, "menu");
////        System.out.println(menu);
//        for (MenuVo menuVo : menu) {
//            System.out.println(menuVo);
//        }
//    }
//
//    @Test
//    public void testMenuUser(){
//        Set<String> menusById = userService.findMenusById("5d9d9fd43017c03018ba9259");
//        Set<HashMap> menuByPerms = userService.findMenuByPerms(menusById);
//        for (HashMap menuByPerm : menuByPerms) {
//            System.out.println(menuByPerm);
//        }
//        System.out.println("========================================");
//
//        List<HashMap> all = userService.findAll();
//        for (HashMap map : all) {
//            System.out.println(map);
//        }
//    }

    @Test
    public void testUserMapper(){
        List<String> admin = userMapper.findAllUrl("admin");
        for (String s : admin) {
            System.out.println(s);
        }
    }

//    @Test
//    public void testJWT(){
//        String admin = JwtHelper.createJWT("admin", null, null);
//        System.out.println("==================================================");
//        System.out.println(admin);
//        System.out.println("==================================================");
//        String test = JwtHelper.createJWT("test", null, null);
//        System.out.println(test);
//        System.out.println("==================================================");
//        Claims claims = JwtHelper.parseJWT(admin);
//        System.out.println(claims);
////        Claims claims1 = JwtHelper.parseJWT("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU3MDY4MTQ3NCwiaWF0IjoxNTcwNjc0Mjc0fQ.C3m6-ejrgpvm4XCPXhw4d-IEpeU3iTBcN7Ix74k9eEd2-95ZL2cEjY9D0oZj2j1t9A0l3tMfAWgoab0zOrjFVg");
////        System.out.println(claims1);
//        String user_name = (String) claims.get("user_name");
//        System.out.println(user_name);
//    }

    @Test
    public void testJWT1(){
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//        String usernameFromToken = jwtTokenUtil.getUsernameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU3MDY4MTQ3NCwiaWF0IjoxNTcwNjc0Mjc0fQ.C3m6-ejrgpvm4XCPXhw4d-IEpeU3iTBcN7Ix74k9eEd2-95ZL2cEjY9D0oZj2j1t9A0l3tMfAWgoab0zOrjFVg");
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU3MDc4MDE4MiwiaWF0IjoxNTcwNzcyOTgyfQ.ehi_TfCTZuJMMBeYc4bW9Kwv0-R2DfO85k8pdwCos4fy8mKP0UN_nFYW9gtqgwoa3AgjnOvGQZtGEdiuKnLIEQ");
        System.out.println(usernameFromToken);
        System.out.println("==================================================");
        String admin = jwtTokenUtil.generateToken("admin");
        System.out.println(admin);
        System.out.println("==================================================");
        String test = jwtTokenUtil.generateToken("test");
        System.out.println(test);
        System.out.println("==================================================");
    }
}
