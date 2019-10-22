package com.asiainfo.security.entity;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/812:30
 */
@Data
@Document("roles")
public class RoleDP {
    private String _id;
    @Field("role_name")
    private String roleName;
    @Field("create_time")
    private Date createTime; // datetime DEFAULT NULL COMMENT '创建日期',
    @Field("last_update_time")
    private Date lastUpdateTime; // datetime DEFAULT NULL COMMENT '最后更能日期',
    @Field("permissions")
    private List<String> permissions;
//    @Field("collection_feilds")
//    private HashMap<String,Set<String>> collectionFeilds;
    @Field("is_delete")
    private boolean isDelete;
//    @Transient
//    private String id;
}
