package com.asiainfo.security.utils;

import com.asiainfo.security.entity.UserDP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @author Mr.LkZ
 * @version 2019/10/1214:05
 */
@Component
public class DataPermissionUtils {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Set<String> getRowPermissions(String username){
        UserDP user = getUserDP(username);
        return user.getAuthorities();
    }

    public Set<String> getFeildsPermissions(String username){
        UserDP user = getUserDP(username);
        return user.getCollectionFeilds().get("news");
    }

//    public Criteria getCriteriaWithRowPermission(Set<String> rows) {
//        return Criteria.where("isDelete").exists(false) //未删除
//                .and("source").in(rows); //设置行权限
//    }

    public Criteria getCriteriaWithRowPermission(String username) {
        Set<String> rows = getRowPermissions(username);
        return Criteria.where("isDelete").exists(false) //未删除
                .and("source").in(rows); //设置行权限
    }

    public void setFeildsPermissions(String username, Query query) {
        Set<String> feilds = getFeildsPermissions(username);
        for (String feild : feilds) {
            query.fields().include(feild);
        }
    }

    public boolean isHaveFeildPermission(String feild,String username){
        if (StringUtils.isEmpty(username))return false;
        Set<String> feildsPermissions = getFeildsPermissions(username);
        if (feildsPermissions == null)return false;
        for (String feildsPermission : feildsPermissions) {
            if (!StringUtils.isEmpty(feildsPermission)&&feildsPermission.equals(feild)){
                return true;
            }
        }
        return false;
    }

    private UserDP getUserDP(String username) {
        Query query = new Query();
        Criteria criteria = Criteria.where("enabled").is(true).and("username").is(username);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query, UserDP.class, "user_dp");
    }
}
