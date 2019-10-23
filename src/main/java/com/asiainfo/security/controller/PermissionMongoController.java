package com.asiainfo.security.controller;

import com.asiainfo.security.entity.criteria.PermissionMongoCriteria;
import com.asiainfo.security.entity.datapermisson.PermissionDp;
import com.asiainfo.security.service.PermissionMongoService;
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
public class PermissionMongoController {
    @Autowired
    private PermissionMongoService permissionMongoService;

    @GetMapping("/permissions/{permissionName}")
    public ResponseEntity getPermissionDp(@PathVariable("permissionName")String permissionName){
        return new ResponseEntity(permissionMongoService.findPermissionDpByName(permissionName),HttpStatus.OK);
    }

//    ("查询角色")
    @GetMapping(value = "/permissions")
    public ResponseEntity getPermissions(PermissionMongoCriteria criteria, Pageable pageable){
        return new ResponseEntity(permissionMongoService.queryAll(criteria,pageable ),HttpStatus.OK);
    }

//    ("新增角色")
    @PostMapping(value = "/permissions")
    public ResponseEntity create(@RequestBody PermissionDp resources){
        if (resources == null|| StringUtils.isEmpty(resources.getPermissionName()))
            throw new RuntimeException("角色名不能为空...");
        return new ResponseEntity(permissionMongoService.create(resources),HttpStatus.CREATED);
    }

//    ("修改角色")
    @PutMapping(value = "/permissions")
    public ResponseEntity update(@RequestBody PermissionDp resources){
        if (resources == null||StringUtils.isEmpty(resources.getPermissionName()))
            throw new RuntimeException("角色名不能为空...");
        permissionMongoService.update(resources);
        return new ResponseEntity(HttpStatus.OK);
    }

//    ("删除角色")
    @DeleteMapping(value = "/permissions/{id}")
    public ResponseEntity delete(@PathVariable String id){
        if (StringUtils.isEmpty(id))throw new RuntimeException("不存在该角色id值...");
        permissionMongoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }



    //    ("创建权限树")
    @GetMapping(value = "/permissions/tree")
    public ResponseEntity createTree(PermissionMongoCriteria criteria){
        return new ResponseEntity(permissionMongoService.createTree(criteria),HttpStatus.OK);
    }
}
