package com.asiainfo.security.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mr.LkZ
 * @version 2019/10/1511:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMongoServiceImplTest {
    @Autowired
    private UserMongoServiceImpl userMongoService;

    @Test
    public void findUserDpByName() {
    }

    @Test
    public void queryAll() {
//        UserMongoCriteria userMongoCriteria = new UserMongoCriteria();
//
//        List<HashMap> list = userMongoService.queryAll(null, null);
//        for (HashMap map : list) {
//            System.out.println(map);
//        }
    }

    @Test
    public void findUserDpByName1() {
//        HashMap user1 = userMongoService.findUserDpByName("user1");
//        System.out.println(user1);
    }

    @Test
    public void queryAll1() {
    }

    @Test
    public void create() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}