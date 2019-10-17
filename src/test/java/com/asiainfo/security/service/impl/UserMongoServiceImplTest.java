package com.asiainfo.security.service.impl;

import com.asiainfo.security.entity.UserDP;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import com.asiainfo.security.service.UserMongoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

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
        UserMongoCriteria userMongoCriteria = new UserMongoCriteria();
//        userMongoCriteria.setUsername("test");
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("source","东方网" );
//        userMongoCriteria.setRow(hashMap);
//        List<HashMap> list = userMongoService.queryAll(userMongoCriteria, null);
//        for (HashMap map : list) {
//            System.out.println(map);
//        }

//        userMongoService.queryAll(userMongoCriteria,null );
//        userMongoCriteria.setFeild("time");
//        HashSet userNameWithFeildPermission = userMongoService.getUserNameWithFeildPermission(userMongoCriteria);
//        System.out.println(userNameWithFeildPermission);
//        UserDP userDP = new UserDP();
//        userDP.setUsername("testCreate11");
//        UserDP userDP1 = userMongoService.create(userDP);
//        System.out.println(userDP1);
        List<HashMap> list = userMongoService.queryAll(null, null);
        for (HashMap map : list) {
            System.out.println(map);
        }
    }
}