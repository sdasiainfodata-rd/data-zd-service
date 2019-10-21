package com.asiainfo.security.service.impl;

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
    public HashMap findUserDpByName(String username){
        Query query = new Query();
        Criteria criteria = Criteria.where("is_delete").is(false).and("username").is(username);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query,HashMap.class ,"user_dp" );
    }

    /**
     * 根据条件分页查询所有用户
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    @Override
    public List<HashMap> queryAll(UserMongoCriteria criteria, Pageable pageable) {
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
        List<HashMap> list = mongoTemplate.find(query, HashMap.class, "user_dp");
        ArrayList<HashMap> users = new ArrayList<>();
        for (HashMap map : list) {
            String _id = map.get("_id").toString();
            map.put("id",_id );
            users.add(map);
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
        if (resources == null|| StringUtils.isEmpty(resources.getUsername()))
            throw new RuntimeException("用户名不能为空...");
        HashMap user = findUserDpByName(resources.getUsername());
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
        if (resources == null||StringUtils.isEmpty(resources.getUsername()))
            throw new RuntimeException("用户名不能为空...");
        if (StringUtils.isEmpty(resources.getId())) throw new RuntimeException("没有用户数据中台id...");
        HashMap user = mongoTemplate.findById(resources.getId(),HashMap.class ,"user_dp");
        if (user==null) throw new RuntimeException("不存在该用户...");
        resources.set_id(user.get("_id").toString());
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
        if (StringUtils.isEmpty(id))throw new RuntimeException("不存在该用户id值...");
        UserDP userDP = mongoTemplate.findById(id, UserDP.class);
        if (userDP==null)throw new RuntimeException("不存在该用户id值...");
        userDP.setDelete(true);
        mongoTemplate.save(userDP,"user_dp" );
    }

}
