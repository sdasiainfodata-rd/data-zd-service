package com.asiainfo.security.controller;

import com.asiainfo.security.entity.datapermisson.RoleDP;
import com.asiainfo.security.entity.datapermisson.UserDP;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import com.asiainfo.security.service.RoleMongoService;
import com.asiainfo.security.service.UserMongoService;
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
public class UserMongoController {
    @Autowired
    private UserMongoService userMongoService;
    @Autowired
    private RoleMongoService roleMongoService;

    @GetMapping("/users/{username}")
    public ResponseEntity getUserDp(@PathVariable("username")String username){
        return new ResponseEntity(userMongoService.findUserDpByName(username),HttpStatus.OK);
    }

//    ("查询用户")
    @GetMapping(value = "/users")
    public ResponseEntity getUsers(UserMongoCriteria criteria, Pageable pageable){
        return new ResponseEntity(userMongoService.queryAll(criteria,pageable ),HttpStatus.OK);
    }

//    ("新增用户")
    @PostMapping(value = "/users")
    public ResponseEntity create(@RequestBody UserDP resources){
        if (resources == null|| StringUtils.isEmpty(resources.getUsername()))
            throw new RuntimeException("用户名不能为空...");
        System.out.println(resources);
        System.out.println(StringUtils.isEmpty(resources.getUsername()));
        isRolesExist(resources);
        return new ResponseEntity(userMongoService.create(resources),HttpStatus.CREATED);
    }



    //    ("修改用户")
    @PutMapping(value = "/users")
    public ResponseEntity update(@RequestBody UserDP resources){
        if (resources == null|| StringUtils.isEmpty(resources.getUsername()))
            throw new RuntimeException("用户名不能为空...");
        if (StringUtils.isEmpty(resources.get_id())) throw new RuntimeException("没有用户数据中台id...");
        isRolesExist(resources);
        userMongoService.update(resources);
        return new ResponseEntity(HttpStatus.OK);
    }

//    ("删除用户")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity delete(@PathVariable String id){
        if (StringUtils.isEmpty(id))throw new RuntimeException("不存在该用户id值...");
        userMongoService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    private void isRolesExist(@RequestBody UserDP resources) {
        List<Object> dataRoles = resources.getDataRoles();
        if (dataRoles!=null){
            for (Object dataRole : dataRoles) {
                if (!isRoleExist((String) dataRole))
                    throw new RuntimeException("角色不存在");
            }
        }
    }

    private boolean isRoleExist(String roleName){
        RoleDP roleDP = roleMongoService.findRoleDpByName(roleName);
        if (roleDP!=null) return true;
        return false;
    }
}
