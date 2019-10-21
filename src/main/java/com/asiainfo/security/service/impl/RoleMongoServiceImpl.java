package com.asiainfo.security.service.impl;

import com.asiainfo.security.entity.RoleDP;
import com.asiainfo.security.entity.UserDP;
import com.asiainfo.security.entity.criteria.RoleMongoCriteria;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import com.asiainfo.security.service.RoleMongoService;
import com.asiainfo.security.service.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Mr.LkZ
 * @version 2019/10/179:40
 */
@Service
public class RoleMongoServiceImpl implements RoleMongoService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据用户名查询可用的用户
     * @param rolename 用户名
     * @return java.util.HashMap
     */
    @Override
    public HashMap findRoleDpByName(String rolename){
        Query query = new Query();
        Criteria criteria = Criteria.where("is_delete").is(false).and("role_name").is(rolename);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query,HashMap.class ,"roles" );
    }

    /**
     * 根据条件分页查询所有用户
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    @Override
    public List<HashMap> queryAll(RoleMongoCriteria criteria, Pageable pageable) {
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
        if (criteria!= null&&!StringUtils.isEmpty(criteria.getRoleName())) {
            //根据criteria获取用户名
            Pattern pattern = Pattern.compile("^.*" + criteria.getRoleName() + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria roleName = Criteria.where("role_name").regex(pattern);
            if (!StringUtils.isEmpty(criteria.getPermission())) {
                String permission = criteria.getPermission();
                roleName.and("permissions").is(permission);
            }
            query.addCriteria(roleName);
        }
        return mongoTemplate.find(query, HashMap.class, "roles");
    }

    /**
     * 插入新的用户
     * @param resources 用户
     * @return com.asiainfo.security.entity.UserDP
     */
    @Override
    public RoleDP create(RoleDP resources) {
        if (resources == null|| StringUtils.isEmpty(resources.getRoleName()))
            throw new RuntimeException("角色名不能为空...");
        HashMap role = findRoleDpByName(resources.getRoleName());
        if (role!=null) throw new RuntimeException("已存在该角色...");
        resources.setCreateTime(new Date());
        resources.setLastUpdateTime(new Date());
        return mongoTemplate.save(resources, "roles");
    }

    /**
     * 更新用户
     * @param resources 用户
     */
    @Override
    public void update(RoleDP resources) {
        if (resources == null||StringUtils.isEmpty(resources.getRoleName()))
            throw new RuntimeException("角色名不能为空...");
        HashMap role = mongoTemplate.findById(resources.get_id(),HashMap.class,"roles" );
        if (role==null) throw new RuntimeException("不存在该角色...");
        resources.set_id(role.get("_id").toString());
        resources.setCreateTime((Date) role.get("create_time"));
        resources.setLastUpdateTime(new Date());
        mongoTemplate.save(resources,"roles" );
    }

    /**
     * 删除用户,实际是将enable设为false,并非真正从数据库删除用户
     * @param id
     */
    @Override
    public void delete(String id) {
        if (StringUtils.isEmpty(id))throw new RuntimeException("不存在该角色id值...");
        RoleDP roleDP = mongoTemplate.findById(id, RoleDP.class);
        if (roleDP==null)throw new RuntimeException("不存在该角色id值...");
        roleDP.setDelete(true);
        mongoTemplate.save(roleDP,"roles" );
    }

}
