package com.asiainfo.security.service;

import com.asiainfo.dataservice.entity.EntityPage;
import com.asiainfo.security.entity.criteria.UserMongoCriteria;
import com.asiainfo.security.entity.datapermisson.UserDP;
import org.springframework.data.domain.Pageable;

/**
 * @author Mr.LkZ
 * @version 2019/10/1510:30
 */
public interface UserMongoService {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return java.util.HashMap
     */
    UserDP findUserDpByName(String username);

    /**
     * 根据条件分页查询所有用户
     * @param criteria 搜索的条件
     * @param pageable 分页
     * @return java.util.List
     */
    EntityPage<UserDP> queryAll(UserMongoCriteria criteria, Pageable pageable);

    /**
     * 插入新的用户
     * @param resources 用户
     * @return com.asiainfo.security.entity.datapermisson.UserDP
     */
    UserDP create(UserDP resources);

    /**
     * 更新用户
     * @param resources 用户
     */
    void update(UserDP resources);

    /**
     * 删除用户,实际是将enable设为false,并非真正从数据库删除用户
     * @param id 用户id
     */
    void delete(String id);


}
