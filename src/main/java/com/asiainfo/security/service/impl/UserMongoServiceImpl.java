package com.asiainfo.security.service.impl;

import com.asiainfo.security.entity.RoleDP;
import com.asiainfo.security.entity.TreeDp;
import com.asiainfo.security.entity.UserDP;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import com.asiainfo.security.service.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Mr.LkZ
 * @version 2019/10/179:40
 */
@Service
public class UserMongoServiceImpl implements UserMongoService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据用户名查询可用的用户
     * @param username 用户名
     * @return java.util.HashMap
     */
    @Override
    public UserDP findUserDpByName(String username){
        Query query = new Query();
        Criteria criteria = Criteria.where("is_delete").is(false).and("username").is(username);
        query.addCriteria(criteria);
        UserDP userDp = mongoTemplate.findOne(query, UserDP.class, "user_dp");
        if (userDp!=null) userDp.set_id(userDp.get_id().toString());//无视警告,可能为空
        return userDp;
    }

    /**
     * 根据条件分页查询所有用户
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    @Override
    public List<UserDP> queryAll(UserMongoCriteria criteria, Pageable pageable) {
        Query query = new Query();
        if (pageable!=null) {
            //分页参数
            int pageSize = pageable.getPageSize();
            int start = (pageable.getPageNumber() - 1) * pageSize;
            query.skip(start);
            query.limit(pageSize);
            //设置排序
            Sort sort = pageable.getSort();
            query.with(sort);
        }
        if (criteria!= null&&!StringUtils.isEmpty(criteria.getUsername())) {
            //根据criteria获取用户名
            Pattern pattern = Pattern.compile("^.*" + criteria.getUsername() + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria username = Criteria.where("username").regex(pattern);
            if (!StringUtils.isEmpty(criteria.getDataRoles())) {
                String dataRole = criteria.getDataRoles();
                username.and("data_roles").is(dataRole);
            }
            query.addCriteria(username);
        }
        List<UserDP> list = mongoTemplate.find(query, UserDP.class, "user_dp");
        ArrayList<UserDP> users = new ArrayList<>();
        for (UserDP user : list) {
            user.set_id(user.get_id().toString());
            users.add(user);
        }
        return users;
    }

    /**
     * 插入新的用户
     * @param resources 用户
     * @return com.asiainfo.security.entity.UserDP
     */
    @Override
    public UserDP create(UserDP resources) {
        UserDP user = findUserDpByName(resources.getUsername());
        if (user!=null) throw new RuntimeException("已存在该用户...");
        resources.setCreateTime(new Date());
        resources.setLastUpdateTime(new Date());
        return mongoTemplate.save(resources, "user_dp");
    }

    /**
     * 更新用户
     * @param resources 用户
     */
    @Override
    public void update(UserDP resources) {
        HashMap user = mongoTemplate.findById(resources.get_id(),HashMap.class ,"user_dp");
        if (user==null) throw new RuntimeException("不存在该用户...");
        resources.setCreateTime((Date) user.get("create_time"));
        resources.setLastUpdateTime(new Date());
        mongoTemplate.save(resources,"user_dp" );
    }

    /**
     * 删除用户,实际是将enable设为false,并非真正从数据库删除用户
     * @param id
     */
    @Override
    public void delete(String id) {
        UserDP userDP = mongoTemplate.findById(id, UserDP.class);
        if (userDP==null)throw new RuntimeException("不存在该用户id值...");
        userDP.setDelete(true);
        mongoTemplate.save(userDP,"user_dp" );
    }



}
