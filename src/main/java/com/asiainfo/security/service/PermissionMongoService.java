package com.asiainfo.security.service;

import com.asiainfo.security.entity.criteria.PermissionMongoCriteria;
import com.asiainfo.security.entity.criteria.RoleMongoCriteria;
import com.asiainfo.security.entity.datapermisson.PermissionDp;
import com.asiainfo.security.entity.datapermisson.RoleDP;
import com.asiainfo.security.entity.datapermisson.TreeDp;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/1510:30
 */
public interface PermissionMongoService {
    /**
     * 根据权限名查询权限
     * @param permissionName 权限名
     * @return java.util.HashMap
     */
    PermissionDp findPermissionDpByName(String permissionName);

    /**
     * 根据条件分页查询所有权限
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    List<PermissionDp> queryAll(PermissionMongoCriteria criteria, Pageable pageable);

    /**
     * 插入新的权限
     * @param resources 权限
     * @return com.asiainfo.security.entity.datapermisson.UserDP
     */
    PermissionDp create(PermissionDp resources);

    /**
     * 更新权限
     * @param resources 权限
     */
    void update(PermissionDp resources);

    /**
     * 删除权限,实际是将enable设为false,并非真正从数据库删除权限
     * @param id
     */
    void delete(String id);

    List<TreeDp> createTree(PermissionMongoCriteria criteria);
}
