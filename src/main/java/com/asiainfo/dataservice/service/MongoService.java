package com.asiainfo.dataservice.service;

import com.asiainfo.dataservice.entity.EntityPage;

import java.util.HashMap;
import java.util.List;

/**
 * 访问Mongo数据库
 * @author Mr.LkZ
 * @version 2019/10/718:07
 */
public interface MongoService {
    List<HashMap> queryGroupByKeyword(String keyword,String username);
    List<HashMap> queryGroupByTime( String condition,String username);
    EntityPage queryAll(Integer num,String username);
}
