package com.asiainfo.security.service;

import com.asiainfo.security.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 用户的登录及权限验证相关的服务
 *
 * @author Mr.LkZ
 * @version 2019/9/302:19
 */
@Service
public class UserService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 用户登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @return java.util.HashMap
     */
    public User userLogin(String username, String password) {
        if (username == null) return null;
        if (password == null) return null;
        Query query = new Query();
        Criteria enabled = Criteria.where("enabled").is(true);
        Criteria criteria = Criteria.where("username").is(username).and("password").is(password);
        query.addCriteria(criteria);
        query.addCriteria(enabled);
        HashMap userMap = mongoTemplate.findOne(query, HashMap.class, "user");
        if (userMap == null || userMap.size() == 0) return null;
        User user = new User();
        user.setUser(userMap);
        List<String> roles = (List<String>) userMap.get("roles");
        if (roles == null || roles.size() == 0) return user;
        ArrayList<HashMap> list = new ArrayList<>();
        for (String role : roles) {
            HashMap roleMap = findRoleByName(role);
            if (roleMap != null) {
                list.add(roleMap);
            }
        }
        user.setRoles(list);

        Object id = userMap.get("_id");
        if (id == null || StringUtils.isEmpty(id.toString())) return user;
        Set<String> urlsById = findUrlsById(id.toString());
        user.setApi_permitions(urlsById);

        Set<String> menusById = findMenusById(id.toString());
        user.setMeanu(menusById);

        return user;
    }

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return java.util.HashMap
     */
    public HashMap<String, Object> findUserByName(String username) {
        Query query = new Query();
        Criteria criteria = Criteria.where("username").is(username);
        Criteria enabled = criteria.where("enabled").is(true);
        query.addCriteria(criteria);
        query.addCriteria(enabled);
        return mongoTemplate.findOne(query, HashMap.class, "user");

    }

    /**
     * 通过_id查找用户
     *
     * @param UserId _id
     * @return java.util.HashMap
     */
    public Set<String> findUrlsById(String UserId) {
        return findPermsById(UserId, "api_permitions");
    }

    public Set<String> findMenusById(String UserId) {
        return findPermsById(UserId, "menu_permitions");
    }

    private Set<String> findPermsById(String UserId, String feildPermitions) {
        if (UserId != null) {
            HashMap user = mongoTemplate.findById(UserId, HashMap.class, "user");
            if (user == null || user.get("enabled") == null) return null;
            boolean enabled = (boolean) user.get("enabled");
            if (enabled) {
                List<String> roles = (List<String>) user.get("roles");
                Set<String> perms = new HashSet<>();
                //遍历用户角色
                for (String role : roles) {
                    Query query = new Query();
                    Criteria criteria = Criteria.where("name").is(role);
                    query.addCriteria(criteria);
                    List<HashMap> listRole = mongoTemplate.find(query, HashMap.class, "roles");
                    //遍历角色权限
                    for (HashMap map : listRole) {
                        List<String> permitions = (List<String>) map.get(feildPermitions);
                        for (String permition : permitions) {
                            perms.add(permition);
                        }
                    }
                }
                return perms;
            }
        }
        return null;
    }

    public void insertOneUser(HashMap<String, Object> hashMap) {
        mongoTemplate.insert(hashMap, "user");
    }

    public HashMap findRoleByName(String roleName) {
        Query query = new Query();
        Criteria criteria = Criteria.where("name").is(roleName);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query, HashMap.class, "roles");
    }
}
