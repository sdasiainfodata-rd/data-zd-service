package com.asiainfo.security.controller;

import com.asiainfo.security.entity.RoleDP;
import com.asiainfo.security.entity.UserDP;
import com.asiainfo.security.entity.criteria.RoleMongoCriteria;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import com.asiainfo.security.service.RoleMongoService;
import com.asiainfo.security.service.UserMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mr.LkZ
 * @version 2019/10/1213:56
 */
@RestController
@RequestMapping("admin")
public class RoleMongoController {
    @Autowired
    private RoleMongoService roleMongoService;

    @GetMapping("/roles/{roleName}")
    public ResponseEntity getUserDp(@PathVariable("roleName")String roleName){
        return new ResponseEntity(roleMongoService.findRoleDpByName(roleName),HttpStatus.OK);
    }

//    ("查询角色")
    @GetMapping(value = "/roles")
    public ResponseEntity getUsers(RoleMongoCriteria criteria, Pageable pageable){
        return new ResponseEntity(roleMongoService.queryAll(criteria,pageable ),HttpStatus.OK);
    }

//    ("新增角色")
    @PostMapping(value = "/roles")
    public ResponseEntity create(@RequestBody RoleDP resources){
        if (resources == null|| StringUtils.isEmpty(resources.getRoleName()))
            throw new RuntimeException("角色名不能为空...");
        return new ResponseEntity(roleMongoService.create(resources),HttpStatus.CREATED);
    }

//    ("修改角色")
    @PutMapping(value = "/roles")
    public ResponseEntity update(@RequestBody RoleDP resources){
        if (resources == null||StringUtils.isEmpty(resources.getRoleName()))
            throw new RuntimeException("角色名不能为空...");
        roleMongoService.update(resources);
        return new ResponseEntity(HttpStatus.OK);
    }

//    ("删除角色")
    @DeleteMapping(value = "/roles/{id}")
    public ResponseEntity delete(@PathVariable String id){
        if (StringUtils.isEmpty(id))throw new RuntimeException("不存在该角色id值...");
        roleMongoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }



    //    ("删除用户")
    @GetMapping(value = "/roles/tree")
    public ResponseEntity createTree(RoleMongoCriteria criteria){
        return new ResponseEntity(roleMongoService.createTree(criteria),HttpStatus.OK);
    }
}
