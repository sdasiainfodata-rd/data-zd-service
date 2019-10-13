package com.asiainfo.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Mr.LkZ
 * @version 2019/10/1217:24
 */
@Service
public class UserMongoService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public HashMap findUserDpByName(String username){
        Query query = new Query();
        Criteria criteria = Criteria.where("enabled").is(true).and("username").is(username);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query,HashMap.class ,"user_dp" );
    }
}
