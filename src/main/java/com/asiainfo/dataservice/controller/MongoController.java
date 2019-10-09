package com.asiainfo.dataservice.controller;

import com.asiainfo.dataservice.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/718:13
 */
@RestController
@RequestMapping("api")
public class MongoController {
    @Autowired
    private MongoService mongoService;

    /**
     * 分页查找数据
     * @param num 页码
     * @return java.util.List
     */
    @RequestMapping("/news/page/{num}")
    public List<HashMap> getMongoData(@PathVariable("num")Integer num){
        return mongoService.queryAll(num);
    }

    /**
     * 用来统计按keyword字段分类后,各类别的数量
     * @param keyword 具体字段
     * @return java.util.List
     */
    @RequestMapping("/news/group/{keyword}")
    public List<HashMap> getGroup(@PathVariable("keyword") String keyword){
        return  mongoService.queryGroupByKeyword(keyword);
        //[{"amount":3,"_id":"type1"},{"amount":5,"_id":"type2"}]
    }

    /**
     * 分时间统计数据的数量
     * @param condition 具体时间 day month year
     * @return java.util.List
     */
    @RequestMapping("/news/time/{condition}")
    public List<HashMap> getGroupByTime(@PathVariable("condition")String condition){
        return mongoService.queryGroupByTime(condition);
        //[{amount=34, _id=2016-05-16}, {amount=137, _id=2018-11-13}]
    }
}
