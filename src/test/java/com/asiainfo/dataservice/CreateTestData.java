package com.asiainfo.dataservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/1816:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateTestData {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void createData(){

        List<HashMap> list = mongoTemplate.find(new Query(), HashMap.class, "news");
        ArrayList<HashMap> news_data_permission = new ArrayList<>();
        for (HashMap map : list) {
            String source = (String) map.get("source");
//            System.out.println(source);
            if (!StringUtils.isEmpty(source)&&source.equals("环球时报-环球网(北京)")){
                ArrayList<String> permissions = new ArrayList<>();
                permissions.add("p1");
                map.put("data_permissions", permissions);
                news_data_permission.add(map);
            }else if (!StringUtils.isEmpty(source)&&source.equals("中国新闻网(北京)")){
                ArrayList<String> permissions = new ArrayList<>();
                permissions.add("p2");
                map.put("data_permissions",permissions );
                news_data_permission.add(map);
            } else if (!StringUtils.isEmpty(source)&&source.equals("环球网")){
                ArrayList<String> permissions = new ArrayList<>();
                permissions.add("p3");
                map.put("data_permissions",permissions );
                news_data_permission.add(map);
            } else if (!StringUtils.isEmpty(source)&&source.equals("环球网军事")){
                ArrayList<String> permissions = new ArrayList<>();
                permissions.add("p1");
                permissions.add("p2");
                map.put("data_permissions",permissions );
                news_data_permission.add(map);
            } else if (!StringUtils.isEmpty(source)&&source.equals("参考防务")){
                news_data_permission.add(map);
            }
        }
        mongoTemplate.insert(news_data_permission,"news_data_permission");
    }
}
