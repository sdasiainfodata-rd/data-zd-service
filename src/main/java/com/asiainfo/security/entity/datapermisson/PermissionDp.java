package com.asiainfo.security.entity.datapermisson;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author Mr.LkZ
 * @version 2019/10/2212:38
 */
@Data
@Document("permissions")
public class PermissionDp {
    private String _id;
    @Field("permission_name")
    private String permissionName;
    @Field("create_time")
    private Date createTime; // datetime DEFAULT NULL COMMENT '创建日期',
    @Field("last_update_time")
    private Date lastUpdateTime; // datetime DEFAULT NULL COMMENT '最后更能日期',
    @Field("is_delete")
    private boolean isDelete;
}
