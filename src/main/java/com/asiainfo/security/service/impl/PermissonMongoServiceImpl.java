package com.asiainfo.security.service.impl;

import com.asiainfo.dataservice.entity.EntityPage;
import com.asiainfo.security.entity.criteria.PermissionMongoCriteria;
import com.asiainfo.security.entity.datapermisson.PermissionDp;
import com.asiainfo.security.entity.datapermisson.RoleDP;
import com.asiainfo.security.entity.datapermisson.TreeDp;
import com.asiainfo.security.service.PermissionMongoService;
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
public class PermissonMongoServiceImpl implements PermissionMongoService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${mongodb.data.permissions}")
    private String permissions;

    /**
     * 根据角色名查询可用的角色
     * @param permissionName 角色名
     * @return java.util.HashMap
     */
    @Override
    public PermissionDp findPermissionDpByName(String permissionName){
        Query query = new Query();
        Criteria criteria = Criteria.where("is_delete").is(false).and("permission_name").is(permissionName);
        query.addCriteria(criteria);
        return mongoTemplate.findOne(query, PermissionDp.class, permissions);
//        if (permissionDp!=null) permissionDp.set_id(permissionDp.get_id().toString());
//        return permissionDp;
    }

    /**
     * 根据条件分页查询所有角色
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    @Override
    public EntityPage<PermissionDp> queryAll(PermissionMongoCriteria criteria, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("is_delete").is(false));
        addCriteria(criteria, query);
        long totalElements = mongoTemplate.count(query, long.class, permissions);
        int num = 0;
        int pageSize = 0;

        if (pageable!=null) {
            //分页参数
            pageSize = pageable.getPageSize();
            num = pageable.getPageNumber();
            CommenUtils.addPageCriteria(pageable, query, num, pageSize);
        }

        List<PermissionDp> list = mongoTemplate.find(query, PermissionDp.class, permissions);
//        ArrayList<PermissionDp> permissionDps = new ArrayList<>();
//        for (PermissionDp permissionDp : list) {
////            permissionDp.set_id(permissionDp.get_id().toString());
//            permissionDps.add(permissionDp);
//        }

        //封装实体页
        EntityPage<PermissionDp> entityPage = new EntityPage<>();
        entityPage.setPage(num);
        entityPage.setSize(pageSize);
        entityPage.setTotalElements(totalElements);
        entityPage.setContent(list);
        return entityPage;
    }


    private void addCriteria(PermissionMongoCriteria criteria, Query query) {
        if (criteria!= null&&!StringUtils.isEmpty(criteria.getPermissionName())) {
            //根据criteria获取角色名
            Pattern pattern = Pattern.compile("^.*" + criteria.getPermissionName() + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria permissionName = Criteria.where("permission_name").regex(pattern);
            query.addCriteria(permissionName);
        }
    }

    /**
     * 插入新的角色
     * @param resources 角色
     * @return com.asiainfo.security.entity.datapermisson.UserDP
     */
    @Override
    public PermissionDp create(PermissionDp resources) {
        PermissionDp permissionDp = findPermissionDpByName(resources.getPermissionName());
        if (permissionDp!=null) throw new RuntimeException("已存在该角色...");
        resources.setCreateTime(new Date());
        resources.setLastUpdateTime(new Date());
        return mongoTemplate.save(resources, permissions);
    }

    /**
     * 更新角色
     * @param resources 角色
     */
    @Override
    public void update(PermissionDp resources) {
        HashMap permission = mongoTemplate.findById(resources.get_id(),HashMap.class,permissions );
        if (permission==null) throw new RuntimeException("不存在该角色...");
//        resources.set_id(role.get("_id").toString());
        resources.setCreateTime((Date) permission.get("create_time"));
        resources.setLastUpdateTime(new Date());
        mongoTemplate.save(resources,permissions );
    }

    /**
     * 删除角色,实际是将enable设为false,并非真正从数据库删除角色
     * @param id 权限id
     */
    @Override
    public void delete(String id) {
        PermissionDp permissionDp = mongoTemplate.findById(id, PermissionDp.class);
        if (permissionDp==null)throw new RuntimeException("不存在该角色id值...");
        if (isPermissionUsedByRoles(permissionDp.getPermissionName()))throw new RuntimeException("该权限正在被角色使用...");
        permissionDp.setDelete(true);
        mongoTemplate.save(permissionDp,permissions );
    }

    /**
     * 创建易于前端显示的结构
     * @param criteria 条件
     * @return java.util.List
     */
    @Override
    public List<TreeDp> createTree(PermissionMongoCriteria criteria) {
        Query query = new Query().addCriteria(Criteria.where("is_delete").is(false));
        addCriteria(criteria,query );
        List<PermissionDp> permissionDps = mongoTemplate.find(query,
                PermissionDp.class, permissions);
        ArrayList<TreeDp> treeDps = new ArrayList<>();
        for (PermissionDp permissionDp : permissionDps) {
            TreeDp treeDp = new TreeDp();
            treeDp.setId(permissionDp.get_id());
            treeDp.setLabel(permissionDp.getPermissionName());
            treeDps.add(treeDp);
        }
        return treeDps;
    }

    private boolean isPermissionUsedByRoles(String permissionsName){
        return !CollectionUtils.isEmpty(findRolesByPermission(permissionsName));
    }

    private List<RoleDP> findRolesByPermission(String permission){
        Query query = new Query();
        Criteria criteria = Criteria.where("is_delete").is(false).and("permissions").is(permission);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, RoleDP.class, "roles");
    }
}
