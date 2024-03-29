package com.asiainfo.dataservice.controller;

import com.asiainfo.dataservice.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @return org.springframework.http.ResponseEntity
     */
    @RequestMapping("/news/page/{num}")
    public ResponseEntity getMongoData(@PathVariable("num")Integer num){
        return new ResponseEntity(mongoService.queryAll(num), HttpStatus.OK);
    }

    /**
     * 用来统计按keyword字段分类后,各类别的数量
     * @param keyword 具体字段
     * @return org.springframework.http.ResponseEntity
     */
    @RequestMapping("/news/group/{keyword}")
    public ResponseEntity getGroup(@PathVariable("keyword") String keyword){
        return new ResponseEntity(mongoService.queryGroupByKeyword(keyword), HttpStatus.OK);
        //[{"amount":3,"_id":"type1"},{"amount":5,"_id":"type2"}]
    }

    /**
     * 分时间统计数据的数量
     * @param condition 具体时间 day month year
     * @return org.springframework.http.ResponseEntity
     */
    @RequestMapping("/news/time/{condition}")
    public ResponseEntity getGroupByTime(@PathVariable("condition")String condition){
        return new ResponseEntity(mongoService.queryGroupByTime(condition), HttpStatus.OK);
        //[{amount=34, _id=2016-05-16}, {amount=137, _id=2018-11-13}]
    }
}
