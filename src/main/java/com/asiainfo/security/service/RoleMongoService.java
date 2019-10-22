package com.asiainfo.security.service;

import com.asiainfo.dataservice.entity.EntityPage;
import com.asiainfo.security.entity.datapermisson.RoleDP;
import com.asiainfo.security.entity.datapermisson.TreeDp;
import com.asiainfo.security.entity.criteria.RoleMongoCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/1510:30
 */
public interface RoleMongoService {
    /**
     * 根据角色名查询角色
     * @param roleName 角色名
     * @return java.util.HashMap
     */
    RoleDP findRoleDpByName(String roleName);

    /**
     * 根据条件分页查询所有角色
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    EntityPage<RoleDP> queryAll(RoleMongoCriteria criteria, Pageable pageable);

    /**
     * 插入新的角色
     * @param resources 角色
     * @return com.asiainfo.security.entity.datapermisson.UserDP
     */
    RoleDP create(RoleDP resources);

    /**
     * 更新角色
     * @param resources 角色
     */
    void update(RoleDP resources);

    /**
     * 删除角色,实际是将enable设为false,并非真正从数据库删除角色
     * @param id
     */
    void delete(String id);

    List<TreeDp> createTree(RoleMongoCriteria criteria);
}
