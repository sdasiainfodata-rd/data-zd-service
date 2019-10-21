package com.asiainfo.security.utils;

import com.asiainfo.security.entity.UserDP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author Mr.LkZ
 * @version 2019/10/1214:05
 */
@Component
public class DataPermissionUtils {
    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 获取用户的行数据权限,并将其封装到查询条件中
     * @param username 用户名
     * @return org.springframework.data.mongodb.core.query.Criteria
     */
    public Criteria getCriteriaWithDataPermissions(String username) {
        HashSet<String> permissions = getPermissionsFromUsername(username);
        if(CollectionUtils.isEmpty(permissions)) return null;//没有任何权限时,返回为空
        if (permissions.contains("admin")) {//如果是管理员权限,不做任何限制
            return new Criteria();
        }
//        Criteria exists = Criteria.where("is_delete").exists(false);
//        Criteria isDelete = Criteria.where("is_delete").is(false);
//        Criteria isNotDelete = new Criteria().orOperator(exists, isDelete);
        //判断有权限存在,数据并没有标记删除
        Criteria criteria = Criteria.where("data_permissions").exists(true).and("is_delete").exists(false);
        HashSet<Criteria> criterias = new HashSet<>();
        for (String permission : permissions) {
            Criteria element = Criteria.where("data_permissions").is(permission);
            criterias.add(element);
        }
        Criteria[] cri = new Criteria[criterias.size()];
        return criteria.orOperator(criterias.toArray(cri));
//        criteria.andOperator((Criteria) CollectionUtils.arrayToList(criterias));
//        return criteria;
    }

    /**
     * 通过用户名查询mongodb中相应的用户数据权限数据
     * @param username 用户名
     * @return com.asiainfo.security.entity.UserDP
     */
    public UserDP getUserDP(String username) {
        Query query = new Query();
        Criteria criteria = Criteria.where("is_delete").is(false).and("data_roles").exists(true)
                .and("username").is(username);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query, UserDP.class, "user_dp");
    }

    /**
     * 获取用户的行数据权限
     * @param username 用户名
     * @return java.util.Set
     */
    private List<String> getDataRoles(String username){
        UserDP user = getUserDP(username);
        if (user == null) return null;
        return user.getDataRoles();
    }

    /**
     * 得到用户数据权限的集合
     * @param roles 角色集合
     * @return java.util.HashSet
     */
    private HashSet<String> getPermissionFromRoles(List<String> roles){
        if (CollectionUtils.isEmpty(roles)) return null;
        HashSet<String> permissions = new HashSet<>();
        //判断管理员权限
        if (roles.contains("admin")){
            permissions.add("admin");
            return permissions;
        }
        //如果是一般用户
        for (String roleName : roles) {
            Criteria exists = Criteria.where(roleName).exists(true);
            HashMap role = mongoTemplate.findOne(new Query().addCriteria(exists), HashMap.class, "roles");
            ArrayList<String> rolePermissions = (ArrayList<String>) role.get(roleName);
            for (String rolePermission : rolePermissions) {
                permissions.add(rolePermission);
            }
        }
        return permissions;
    }

    /**
     * 根据用户名得到数据权限集合
     * @param username 用户名
     * @return java.util.HashSet
     */
    private HashSet<String> getPermissionsFromUsername(String username){
        List<String> dataRoles = getDataRoles(username);
        return getPermissionFromRoles(dataRoles);
    }
}
