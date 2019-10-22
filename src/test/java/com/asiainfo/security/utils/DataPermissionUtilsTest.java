package com.asiainfo.security.utils;

import com.asiainfo.security.entity.datapermisson.UserDP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/1710:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataPermissionUtilsTest {
    @Autowired
    private DataPermissionUtils dataPermissionUtils;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void creatUser() {
//        UserDP userDP = new UserDP();
//        userDP.setCreateTime(new Date());
//        userDP.setLastUpdateTime(new Date());
//        userDP.setUsername("user2");
////        userDP.setDataRoles("admin");
//        ArrayList<String> roles = new ArrayList<>();
//        roles.add("role1");
//        roles.add("role2");
//
//
//        userDP.setDataRoles(roles);
//        mongoTemplate.save(userDP,"user_dp");
    }

    @Test
    public void getCriteriaWithDataPermission() {



        Criteria user1 = dataPermissionUtils.getCriteriaWithDataPermissions("user1");
        Criteria user2 = dataPermissionUtils.getCriteriaWithDataPermissions("user2");

        Query query = new Query();
        query.addCriteria(user2);
//        Criteria criteria = Criteria.where("data_permissions").is("p1");
//        query.addCriteria(criteria);
        List<HashMap> list = mongoTemplate.find(query, HashMap.class, "news_test");
        System.out.println("========================================================");
        for (HashMap map : list) {
            System.out.println(map);
        }
    }

    @Test
    public void testRoles(){
        Criteria exists = Criteria.where("role1").exists(true);
        HashMap role = mongoTemplate.findOne(new Query().addCriteria(exists), HashMap.class, "roles");
        ArrayList<String> rolePermissions = (ArrayList<String>) role.get("role1");
        for (String rolePermission : rolePermissions) {
            System.out.println(rolePermission);
        }
    }

    @Test
    public void showToken(){
        createAndPrintToken("userP1");
        createAndPrintToken("userP2");
        createAndPrintToken("userP3");
        createAndPrintToken("userP1P2");
        createAndPrintToken("userTwoRole");
        createAndPrintToken("userP1P2P3");
        createAndPrintToken("userAdmin");
        createAndPrintToken("userEmpty");
        createAndPrintToken("userNull");
    }

    private void createAndPrintToken(String username) {
        System.out.println("======================================================");
        String token = jwtTokenUtil.generateToken(username);
        System.out.println(username+":token:  "+token);
        System.out.println("======================================================");
    }
}