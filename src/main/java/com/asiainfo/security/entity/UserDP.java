package com.asiainfo.security.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Mr.LkZ
 * @version 2019/10/812:30
 */
@Data
public class UserDP {
    private String username;
    @Field("create_time")
    private Date createTime; // datetime DEFAULT NULL COMMENT '创建日期',
    @Field("last_update_time")
    private Date lastUpdateTime; // datetime DEFAULT NULL COMMENT '最后更能日期',
    private Set<List<HashMap<String,String>>> authorities;
    @Field("collection_feilds")
    private HashMap<String,Set<String>> collectionFeilds;
    private boolean enabled;
}
