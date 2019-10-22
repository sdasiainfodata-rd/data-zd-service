package com.asiainfo.security.mapper;

import com.asiainfo.security.entity.permission.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/9/179:32
 */
@Mapper
@Repository
public interface UserMapper {
    User findUser(@Param("username") String username);
    List<String> findAllUrl(@Param("username") String username);
}
