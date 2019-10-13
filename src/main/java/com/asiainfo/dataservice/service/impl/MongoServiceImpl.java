package com.asiainfo.dataservice.service.impl;

import com.asiainfo.dataservice.entity.EntityPage;
import com.asiainfo.dataservice.service.MongoService;
import com.asiainfo.security.utils.DataPermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

/**
 * 访问Mongo数据库
 * @author Mr.LkZ
 * @version 2019/10/718:09
 */
@Service
public class MongoServiceImpl implements MongoService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DataPermissionUtils dataPermissionUtils;

    /**
     * 根据数据的一个字段统计字段中相同值的数量
     * @param keyword 数据字段名
     * @return java.util.List
     */
    @Override
    public List<HashMap> queryGroupByKeyword(String keyword,String username) {
        //判断是否有字段权限
        if (!dataPermissionUtils.isHaveFeildPermission(keyword, username)) return null;
        //聚合 过滤存在isDelete字段的数据,根据keyword字段分类 并统计各个类的数据总数,存入amount字段
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(dataPermissionUtils.getCriteriaWithRowPermission(username)),//设置行权限
                Aggregation.group(keyword).count().as("amount"),
                Aggregation.sort(Sort.Direction.DESC,"amount")
        );
        //collectionName是mongodb中数据仓库的名字,应该从配置文件中获得,或者前台返回,暂时写死
        AggregationResults<HashMap> aggregate = mongoTemplate.aggregate(agg,"news", HashMap.class);
        ArrayList<HashMap> list = new ArrayList<>();
        list.addAll(aggregate.getMappedResults());

        return list;
    }

    /**
     * 按时间段统计数据的数量
     * @param condition 时间 可以是day month year
     * @return java.util.List
     */
    @Override
    public List<HashMap> queryGroupByTime(String condition,String username) {
        //判断是否有字段权限
        if (!dataPermissionUtils.isHaveFeildPermission("time", username)) return null;
        int start = 0;
        int len = 10;
        if ("month".equals(condition)){
            start = 0;
            len = 7;
        }
        if ("year".equals(condition)){
            start = 0;
            len = 4;
        }

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(dataPermissionUtils.getCriteriaWithRowPermission(username)),//设置行权限
                project().andExpression( "substr(time,"+start+","+len+")").as("day"),//substr中8代表开始位置,向后取两位
                Aggregation.group("day").count().as("amount"),
                Aggregation.sort(Sort.Direction.DESC,"amount")
        );
        AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(agg,"news" ,HashMap.class);
        ArrayList<HashMap> list = new ArrayList<>();
        list.addAll(aggregationResults.getMappedResults());
        return list;
    }

    /**
     * 分页查找数据
     * @param num 页码
     * @return java.util.List
     */
    @Override
    public EntityPage queryAll(Integer num,String username) {
        //设置行权限
        Criteria criteria = dataPermissionUtils.getCriteriaWithRowPermission(username);
        if (criteria == null) return new EntityPage();

        long totalElements = mongoTemplate.count(new Query().addCriteria(criteria), long.class, "news");
        Query query = new Query();
        //分页参数
        int pageSize = 10;
        int start = (num - 1) * pageSize;
        query.skip(start);
        query.limit(pageSize);


        //按行权限排序
        query.with(new Sort(Sort.Direction.ASC,"source"));
        //设置字段权限
        dataPermissionUtils.setFeildsPermissions(username, query);
        query.addCriteria(criteria);

        //collectionName是mongodb中数据仓库的名字,应该从配置文件中获得,或者前台返回,暂时写死
        ArrayList<HashMap> list = (ArrayList<HashMap>) mongoTemplate.find(query, HashMap.class, "news");
        //封装实体页
        EntityPage entityPage = new EntityPage();
        entityPage.setPage(num);
        entityPage.setSize(pageSize);
        entityPage.setTotalElements(totalElements);
        entityPage.setContent(list);
        return entityPage;
    }


}
