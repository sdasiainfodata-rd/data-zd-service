package com.asiainfo.security.entity.permission;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * 查询sql中的实体类
 * @author Mr.LkZ
 * @version 2019/10/812:30
 */
//用来判断url权限
@Data
public class User {
    private Long id; // bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    private String avatar; // varchar(255) DEFAULT NULL COMMENT '头像地址',
    private Date create_time; // datetime DEFAULT NULL COMMENT '创建日期',
    private String email; // varchar(255) DEFAULT NULL COMMENT '邮箱',
    private String password; // varchar(255) DEFAULT NULL COMMENT '密码',
    private String username; // varchar(255) DEFAULT NULL COMMENT '用户名',
    private Date last_password_reset_time; // datetime DEFAULT NULL COMMENT '最后修改密码的日期',
    private Long dept_id; // bigint(20) DEFAULT NULL,
    private String phone; // varchar(255) DEFAULT NULL,
    private Long job_id; // bigint(20) DEFAULT NULL,
    private Set<String> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
