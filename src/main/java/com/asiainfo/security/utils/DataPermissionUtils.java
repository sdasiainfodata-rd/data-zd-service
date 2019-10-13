package com.asiainfo.security.utils;

import com.asiainfo.security.entity.UserDP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Mr.LkZ
 * @version 2019/10/1214:05
 */
@Component
public class DataPermissionUtils {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 获取用户的行数据权限
     * @param username 用户名
     * @return java.util.Set
     */
    public Set<List<HashMap<String,String>>> getRowPermissions(String username){
        UserDP user = getUserDP(username);
        if (user == null) return null;
        return user.getAuthorities();
    }

    /**
     * 获取用户的字段权限
     * @param username 用户名
     * @return java.util.Set
     */
    public Set<String> getFeildsPermissions(String username){
        UserDP user = getUserDP(username);
        return user.getCollectionFeilds().get("news");
    }

//    public Criteria getCriteriaWithRowPermission(Set<String> rows) {
//        return Criteria.where("isDelete").exists(false) //未删除
//                .and("source").in(rows); //设置行权限
//    }

    /**
     * 获取用户的行数据权限,并将其封装到查询条件中
     * @param username 用户名
     * @return org.springframework.data.mongodb.core.query.Criteria
     */
    public Criteria getCriteriaWithRowPermission(String username) {
        Set<List<HashMap<String,String>>> rows = getRowPermissions(username);
        if(rows == null || rows.size() == 0) return null;//没有任何权限时,返回为空

        if (rows.size() == 1){
            for (List<HashMap<String, String>> row : rows) {
                if (row!=null&&row.size()==1||row.get(0).get("admin")!=null){
                    return new Criteria();
                }
            }
        }

        Criteria criteria = Criteria.where("isDelete").exists(false);
        HashSet<Criteria> criteriasSet = new HashSet<>();
        for (List<HashMap<String,String>> row : rows) {
            if (row==null||row.size()==0)continue;
            Criteria criteriaRow = new Criteria();
            for (HashMap<String,String> map : row) {
                String feild =  map.get("feild");
                String value = map.get("value");
                //包含value
                Pattern pattern= Pattern.compile("^.*"+value+".*$", Pattern.CASE_INSENSITIVE);
                criteriaRow.and(feild).regex(pattern);
            }
            criteriasSet.add(criteriaRow);
        }
        Criteria[] cri = new Criteria[criteriasSet.size()];
        criteria.orOperator(criteriasSet.toArray(cri));
        return criteria;
    }

    /**
     * 向查询条件中添加对应用户的字段条件限制
     * @param username 用户名
     * @param query 查询条件
     */
    public void setFeildsPermissions(String username, Query query) {
        Set<String> feilds = getFeildsPermissions(username);
        if (feilds == null||feilds.size()==0){
            throw new RuntimeException("没有授权字段...");
        }
        for (String feild : feilds) {
            query.fields().include(feild);
        }
    }

    /**
     * 判断用户是否有该字段的数据权限
     * @param feild 字段
     * @param username 用户名
     * @return boolean
     */
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

    public Set<String> getFeildsFromRowPermission(String username){
        Set<List<HashMap<String, String>>> rowPermissions = getRowPermissions(username);
        if (rowPermissions == null)return new HashSet<>();
        HashSet<String> feilds = new HashSet<>();
        for (List<HashMap<String, String>> rowPermission : rowPermissions) {
            if (rowPermission==null) continue;
            for (HashMap<String, String> map : rowPermission) {
                if (map==null||map.size()==0)continue;
                String feild = map.get("feild");
                if (!StringUtils.isEmpty(feild))feilds.add(feild);
            }
        }
        return feilds;
    }

    /**
     * 通过用户名查询mongodb中相应的用户数据权限数据
     * @param username 用户名
     * @return com.asiainfo.security.entity.UserDP
     */
    private UserDP getUserDP(String username) {
        Query query = new Query();
        Criteria criteria = Criteria.where("enabled").is(true).and("username").is(username);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query, UserDP.class, "user_dp");
    }
}
