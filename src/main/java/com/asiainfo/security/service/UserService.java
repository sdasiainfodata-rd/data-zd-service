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
     * @return com.asiainfo.security.entity.User
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
        //设置user
        user.setUser(userMap);
        //设置roles
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
        //设置apipermitions
        Set<String> urlsById = findUrlsById(id.toString());
        user.setApiPermitions(urlsById);
        //设置meanu
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
     * 通过_id查找用户的所有api权限
     *
     * @param UserId _id
     * @return java.util.Set
     */
    public Set<String> findUrlsById(String UserId) {
        return findPermsById(UserId, "api_permitions");
    }

    /**
     * 通过_id查找用户的所有菜单权限
     *
     * @param UserId _id
     * @return java.util.Set
     */
    public Set<String> findMenusById(String UserId) {
        return findPermsById(UserId, "menu_permitions");
    }

    /**
     * 通过_id和字段查找用户的相关权限
     * @param UserId _id
     * @param feildPermitions roles中的相关权限字段
     * @return java.util.Set
     */
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
                        if (permitions == null)return perms;
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

    /**
     * 添加一个用户
     * @param hashMap 用户的各个参数
     */
    public void insertOneUser(HashMap<String, Object> hashMap) {
        mongoTemplate.insert(hashMap, "user");
    }

    /**
     * 通过roleName查找roles中的role记录
     * @param roleName 角色名
     * @return java.util.HashMap
     */
    public HashMap findRoleByName(String roleName) {
        Query query = new Query();
        Criteria criteria = Criteria.where("name").is(roleName);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query, HashMap.class, "roles");
    }

    /**
     * 通过用户的权限查找对应的菜单实体
     * @param perms 用户拥有的全部菜单权限
     * @return java.util.Set
     */
    public Set<HashMap> findMenuByPerms(Set<String> perms){
        HashSet<HashMap> menu = new HashSet<>();
        for (String perm : perms) {
            Query query = new Query();
            Criteria criteria = Criteria.where("path").is(perm);
            query.addCriteria(criteria);
            HashMap one = mongoTemplate.findOne(query, HashMap.class, "menu");
            menu.add(one);
        }
        return menu;
    }

    public List<HashMap> findAll(){
        Query query = new Query();
        Criteria criteria = Criteria.where("enabled").is(true);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, HashMap.class, "user");
    }
}
