package com.asiainfo.security.controller;

import com.asiainfo.security.entity.criteria.RoleMongoCriteria;
import com.asiainfo.security.entity.datapermisson.PermissionDp;
import com.asiainfo.security.entity.datapermisson.RoleDP;
import com.asiainfo.security.service.PermissionMongoService;
import com.asiainfo.security.service.RoleMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/1213:56
 */
@RestController
@RequestMapping("admin")
public class RoleMongoController {
    @Autowired
    private RoleMongoService roleMongoService;
    @Autowired
    private PermissionMongoService permissionMongoService;

    @GetMapping("/roles/{roleName}")
    public ResponseEntity getRoleDp(@PathVariable("roleName")String roleName){
        return new ResponseEntity(roleMongoService.findRoleDpByName(roleName),HttpStatus.OK);
    }

//    ("查询角色")
    @GetMapping(value = "/roles")
    public ResponseEntity getRoles(RoleMongoCriteria criteria, Pageable pageable){
        return new ResponseEntity(roleMongoService.queryAll(criteria,pageable ),HttpStatus.OK);
    }

//    ("新增角色")
    @PostMapping(value = "/roles")
    public ResponseEntity create(@RequestBody RoleDP resources){
        if (resources == null|| StringUtils.isEmpty(resources.getRoleName()))
            throw new RuntimeException("角色名不能为空...");
        isPermissionsExist(resources);
        return new ResponseEntity(roleMongoService.create(resources),HttpStatus.CREATED);
    }

//    ("修改角色")
    @PutMapping(value = "/roles")
    public ResponseEntity update(@RequestBody RoleDP resources){
        if (resources == null||StringUtils.isEmpty(resources.getRoleName()))
            throw new RuntimeException("角色名不能为空...");
        isPermissionsExist(resources);
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



    //    ("创建角色树")
    @GetMapping(value = "/roles/tree")
    public ResponseEntity createTree(RoleMongoCriteria criteria){
        return new ResponseEntity(roleMongoService.createTree(criteria),HttpStatus.OK);
    }

    private void isPermissionsExist(@RequestBody RoleDP resources) {
        List<Object> dataPermissions = resources.getPermissions();
        if (dataPermissions!=null){
            for (Object dataPermission : dataPermissions) {
                if (!isPermissionExist((String) dataPermission))
                    throw new RuntimeException("角色不存在");
            }
        }
    }

    private boolean isPermissionExist(String permissionName){
        PermissionDp permissionDp = permissionMongoService.findPermissionDpByName(permissionName);
        return permissionDp != null;
    }
}
