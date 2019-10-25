package com.asiainfo.security.service.impl;

import com.asiainfo.dataservice.entity.EntityPage;
import com.asiainfo.security.entity.criteria.RoleMongoCriteria;
import com.asiainfo.security.entity.datapermisson.PermissionDp;
import com.asiainfo.security.entity.datapermisson.RoleDP;
import com.asiainfo.security.entity.datapermisson.TreeDp;
import com.asiainfo.security.entity.datapermisson.UserDP;
import com.asiainfo.security.service.PermissionMongoService;
import com.asiainfo.security.service.RoleMongoService;
import com.asiainfo.security.utils.CommenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    @Autowired
    private PermissionMongoService permissionMongoService;
    @Value("${mongodb.data.roles}")
    private String roles;

    /**
     * 根据角色名查询可用的角色
     * @param rolename 角色名
     * @return java.util.HashMap
     */
    @Override
    public RoleDP findRoleDpByName(String rolename){
        Query query = new Query();
        Criteria criteria = Criteria.where("is_delete").is(false).and("role_name").is(rolename);
        query.addCriteria(criteria);
        RoleDP role = mongoTemplate.findOne(query, RoleDP.class, roles);
        if (role!=null) role.setPermissions(getPermissionEntiyList(role.getPermissions()));
        return role;
    }

    /**
     * 根据条件分页查询所有角色
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    @Override
    public EntityPage<RoleDP> queryAll(RoleMongoCriteria criteria, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("is_delete").is(false));
        addCriteria(criteria, query);

        long totalElements = mongoTemplate.count(query, long.class, roles);
        int num = 0;
        int pageSize = 0;

        if (pageable!=null) {
            //分页参数
            pageSize = pageable.getPageSize();
            num = pageable.getPageNumber();
            CommenUtils.addPageCriteria(pageable, query, num, pageSize);
        }

        List<RoleDP> list = mongoTemplate.find(query, RoleDP.class, roles);
//        ArrayList<RoleDP> roles = new ArrayList<>();
        for (RoleDP role : list) {
            List<Object> permissions = role.getPermissions();
            if (permissions == null)continue;
            role.setPermissions(getPermissionEntiyList(permissions));
//            role.set_id(role.get_id().toString());
//            roles.add(role);
        }

        //封装实体页
        EntityPage<RoleDP> entityPage = new EntityPage<>();
        entityPage.setPage(num);
        entityPage.setSize(pageSize);
        entityPage.setTotalElements(totalElements);
        entityPage.setContent(list);
        return entityPage;
    }


    private ArrayList<Object> getPermissionEntiyList(List<Object> permissions) {
        if (CollectionUtils.isEmpty(permissions)) return null;
        ArrayList<Object> permissionDps = new ArrayList<>();
        for (Object permission : permissions) {
            String permissionName = (String) permission;
            PermissionDp permissionDp = permissionMongoService.findPermissionDpByName(permissionName);
            permissionDps.add(permissionDp);
        }
        return permissionDps;
    }

    private void addCriteria(RoleMongoCriteria criteria, Query query) {
        if (criteria!= null&&!StringUtils.isEmpty(criteria.getRoleName())) {
            //根据criteria获取角色名
            Pattern pattern = Pattern.compile("^.*" + criteria.getRoleName() + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria roleName = Criteria.where("role_name").regex(pattern);
            if (!StringUtils.isEmpty(criteria.getPermission())) {
                String permission = criteria.getPermission();
                roleName.and("permissions").is(permission);
            }
            query.addCriteria(roleName);
        }
    }

    /**
     * 插入新的角色
     * @param resources 角色
     * @return com.asiainfo.security.entity.datapermisson.UserDP
     */
    @Override
    public RoleDP create(RoleDP resources) {
        RoleDP role = findRoleDpByName(resources.getRoleName());
        if (role!=null) throw new RuntimeException("已存在该角色...");
        resources.setCreateTime(new Date());
        resources.setLastUpdateTime(new Date());
        return mongoTemplate.save(resources, roles);
    }

    /**
     * 更新角色
     * @param resources 角色
     */
    @Override
    public void update(RoleDP resources) {
        HashMap role = mongoTemplate.findById(resources.get_id(),HashMap.class,roles );
        if (role==null) throw new RuntimeException("不存在该角色...");
//        resources.set_id(role.get("_id").toString());
        resources.setCreateTime((Date) role.get("create_time"));
        resources.setLastUpdateTime(new Date());
        mongoTemplate.save(resources,roles );
    }

    /**
     * 删除角色,实际是将enable设为false,并非真正从数据库删除角色
     * @param id 角色id
     */
    @Override
    public void delete(String id) {
        RoleDP roleDP = mongoTemplate.findById(id, RoleDP.class);
        if (roleDP==null)throw new RuntimeException("不存在该角色id值...");
        if (isRoleUsedByUsers(roleDP.getRoleName()))throw new RuntimeException("该角色正在被用户使用...");
        roleDP.setDelete(true);
        mongoTemplate.save(roleDP,roles );
    }

    /**
     * 创建易于前端显示的结构
     * @param criteria 条件
     * @return java.util.List
     */
    @Override
    public List<TreeDp> createTree(RoleMongoCriteria criteria) {
        Query query = new Query().addCriteria(Criteria.where("is_delete").is(false));
        addCriteria(criteria,query );
        List<RoleDP> roleDPS = mongoTemplate.find(query,
                RoleDP.class, roles);
        ArrayList<TreeDp> treeDps = new ArrayList<>();
        for (RoleDP roleDP : roleDPS) {
            TreeDp treeDp = new TreeDp();
            treeDp.setId(roleDP.get_id());
            treeDp.setLabel(roleDP.getRoleName());
            treeDps.add(treeDp);
        }

        return treeDps;
    }

    private boolean isRoleUsedByUsers(String RoleName){
        return !CollectionUtils.isEmpty(findUsersByRole(RoleName));
    }

    private List<UserDP> findUsersByRole(String RoleName){
        Query query = new Query();
        Criteria criteria = Criteria.where("is_delete").is(false).and("data_roles").is(RoleName);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, UserDP.class, "user_dp");
    }
}
