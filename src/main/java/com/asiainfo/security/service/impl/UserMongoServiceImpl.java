package com.asiainfo.security.service.impl;

import com.asiainfo.security.entity.UserDP;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import com.asiainfo.security.service.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Mr.LkZ
 * @version 2019/10/1217:24
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
    public HashMap findUserDpByName(String username){
        Query query = new Query();
        Criteria criteria = Criteria.where("enabled").is(true).and("username").is(username);
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
        if (criteria!= null) {
            //根据criteria获取用户名
            Pattern pattern = Pattern.compile("^.*" + criteria.getUsername() + ".*$", Pattern.CASE_INSENSITIVE);
            HashSet usersRow = getUserNameWithRowPermission(criteria);
            HashSet usersFeild = getUserNameWithFeildPermission(criteria);

            Criteria username = Criteria.where("username").regex(pattern).and("username").in(usersRow)
                    .and("username").in(usersFeild);
            query.addCriteria(username);
        }
        return mongoTemplate.find(query,HashMap.class ,"user_dp" );
    }

    /**
     * 插入新的用户
     * @param resources 用户
     * @return com.asiainfo.security.entity.UserDP
     */
    @Override
    public UserDP create(UserDP resources) {
        if (resources == null||StringUtils.isEmpty(resources.getUsername()))
            throw new RuntimeException("用户名不能为空...");
        HashMap user = findUserDpByName(resources.getUsername());
        if (user!=null) throw new RuntimeException("已存在该用户...");
        resources.setCreateTime(new Date());
        resources.setLastUpdateTime(new Date());
        resources.setEnabled(true);
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
        HashMap user = findUserDpByName(resources.getUsername());
        if (user==null) throw new RuntimeException("不存在该用户...");
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
        userDP.setEnabled(false);
        mongoTemplate.save(userDP,"user_dp" );
    }


    /**
     * 查询满足行权限搜索条件的用户名
     * @param criteria 搜索条件
     * @return java.util.HashSet
     */
    private HashSet getUserNameWithRowPermission(UserMongoCriteria criteria) {
        if (criteria==null)return null;
        HashMap row = criteria.getRow();
        if (row==null||row.size()==0)return new HashSet();
        Object feild = row.keySet().toArray()[0];
        Object value = row.get(feild);
        if (StringUtils.isEmpty(feild)||StringUtils.isEmpty(value))return new HashSet();
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.unwind("authorities"),
                Aggregation.unwind("authorities"),
                Aggregation.match(Criteria.where("authorities.feild").is(feild).and("authorities.value").is(value)),
                Aggregation.group("username")
        );
        AggregationResults<HashMap> users = mongoTemplate.aggregate(agg, "user_dp", HashMap.class);
        HashSet set = new HashSet<>();
        for (HashMap map : users) {
            set.add(map.get("_id"));
        }
        return set;
    }

    /**
     * 查询满足列权限搜索条件的用户名
     * @param criteria 搜索条件
     * @return java.util.HashSet
     */
    private HashSet getUserNameWithFeildPermission(UserMongoCriteria criteria) {
        if (criteria==null)return null;
        String feild = criteria.getFeild();
        if (StringUtils.isEmpty(feild))return new HashSet();
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.unwind("collection_feilds.news"),
                Aggregation.match(Criteria.where("collection_feilds.news").is(feild)),
                Aggregation.group("username")
        );
        AggregationResults<HashMap> users = mongoTemplate.aggregate(agg, "user_dp", HashMap.class);
        HashSet set = new HashSet<>();
        for (HashMap map : users) {
            set.add(map.get("_id"));
        }
        return set;
    }


}
