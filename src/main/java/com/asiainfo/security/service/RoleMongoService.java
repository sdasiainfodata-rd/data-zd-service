package com.asiainfo.security.service;

import com.asiainfo.security.entity.RoleDP;
import com.asiainfo.security.entity.UserDP;
import com.asiainfo.security.entity.criteria.RoleMongoCriteria;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/1510:30
 */
public interface RoleMongoService {
    /**
     * 根据角色名查询用户
     * @param roleName 角色名
     * @return java.util.HashMap
     */
    HashMap findRoleDpByName(String roleName);

    /**
     * 根据条件分页查询所有角色
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    List<HashMap> queryAll(RoleMongoCriteria criteria, Pageable pageable);

    /**
     * 插入新的用户
     * @param resources 用户
     * @return com.asiainfo.security.entity.UserDP
     */
    RoleDP create(RoleDP resources);

    /**
     * 更新用户
     * @param resources 用户
     */
    void update(RoleDP resources);

    /**
     * 删除用户,实际是将enable设为false,并非真正从数据库删除用户
     * @param id
     */
    void delete(String id);
}
