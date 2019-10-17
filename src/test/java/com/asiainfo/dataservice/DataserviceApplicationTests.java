package com.asiainfo.dataservice;

import com.asiainfo.security.entity.UserDP;
import com.asiainfo.security.mapper.UserMapper;
import com.asiainfo.security.service.impl.UserMongoServiceImpl;
import com.asiainfo.security.utils.DataPermissionUtils;
import com.asiainfo.security.utils.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataserviceApplicationTests {
//    @Autowired
//    private UserService userService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserMongoServiceImpl userMongoServiceImpl;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private DataPermissionUtils dataPermissionUtils;

//    @Test
//    public void testUser(){
//        HashMap tom = userService.findUserByName("tom");
//        List api_permitions = (List) tom.get("apiPermitions");
//        for (Object api_permition : api_permitions) {
//            System.out.println(api_permition.toString());
//        }
//        System.out.println(tom);
//        System.out.println("=================================================");
//
//        Object id = tom.get("_id");
//        String s = id.toString();
//        Set<String> urls = userService.findUrlsById(s);
//        for (String url : urls) {
//            System.out.println(url);
//        }
//        System.out.println("=======================================");
//        Set<String> urlsById = userService.findUrlsById("5d9c75fc0dd2c2ea602aa893");
//        System.out.println(urlsById);
//        HashMap xiaokong = userService.findUserByName("xiaokong");
//        System.out.println(xiaokong);
////        HashMap<String, Object> xiaokong1 = userService.userLogin("xiaokong", "123");
////        System.out.println(xiaokong1);
//    }
//
//    @Test
//    public void testUser1(){
//        HashMap tom = userService.findUserByName("bajie");
//        System.out.println(tom);
//        Object roles = tom.get("roles");
//        System.out.println(roles);
//
//        Object id = tom.get("_id");
//        String s = id.toString();
//        Set<String> urlsById = userService.findUrlsById(s);
//        for (String s1 : urlsById) {
//            System.out.println(s1);
//        }
//
//        UserMG map = userService.userLogin("bajie", "123");
//        System.out.println(map);
//    }
//
//    @Test
//    public void testMenu(){
//        Query query = new Query();
//        List<MenuVo> menu = mongoTemplate.find(query, MenuVo.class, "menu");
////        System.out.println(menu);
//        for (MenuVo menuVo : menu) {
//            System.out.println(menuVo);
//        }
//    }
//
//    @Test
//    public void testMenuUser(){
//        Set<String> menusById = userService.findMenusById("5d9d9fd43017c03018ba9259");
//        Set<HashMap> menuByPerms = userService.findMenuByPerms(menusById);
//        for (HashMap menuByPerm : menuByPerms) {
//            System.out.println(menuByPerm);
//        }
//        System.out.println("========================================");
//
//        List<HashMap> all = userService.findAll();
//        for (HashMap map : all) {
//            System.out.println(map);
//        }
//    }

    @Test
    public void showFeilds(){
        Set<String> feilds = dataPermissionUtils.getFeildsFromRowPermission("testSourceAndEditor");
        for (String feild : feilds) {
            System.out.println(feild);
        }
        System.out.println("====================================");
        Set<String> testNoFeilds = dataPermissionUtils.getFeildsPermissions("testNoFeilds");
        System.out.println(testNoFeilds);
    }

    @Test
    public void testUserMapper(){
        List<String> admin = userMapper.findAllUrl("admin");
        for (String s : admin) {
            System.out.println(s);
        }
    }

//    @Test
//    public void testJWT(){
//        String admin = JwtHelper.createJWT("admin", null, null);
//        System.out.println("==================================================");
//        System.out.println(admin);
//        System.out.println("==================================================");
//        String test = JwtHelper.createJWT("test", null, null);
//        System.out.println(test);
//        System.out.println("==================================================");
//        Claims claims = JwtHelper.parseJWT(admin);
//        System.out.println(claims);
////        Claims claims1 = JwtHelper.parseJWT("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU3MDY4MTQ3NCwiaWF0IjoxNTcwNjc0Mjc0fQ.C3m6-ejrgpvm4XCPXhw4d-IEpeU3iTBcN7Ix74k9eEd2-95ZL2cEjY9D0oZj2j1t9A0l3tMfAWgoab0zOrjFVg");
////        System.out.println(claims1);
//        String user_name = (String) claims.get("user_name");
//        System.out.println(user_name);
//    }

    @Test
    public void testJWT1(){
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//        String usernameFromToken = jwtTokenUtil.getUsernameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU3MDY4MTQ3NCwiaWF0IjoxNTcwNjc0Mjc0fQ.C3m6-ejrgpvm4XCPXhw4d-IEpeU3iTBcN7Ix74k9eEd2-95ZL2cEjY9D0oZj2j1t9A0l3tMfAWgoab0zOrjFVg");
        String usernameFromToken = jwtTokenUtil.getUsernameFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU3MDc4MDE4MiwiaWF0IjoxNTcwNzcyOTgyfQ.ehi_TfCTZuJMMBeYc4bW9Kwv0-R2DfO85k8pdwCos4fy8mKP0UN_nFYW9gtqgwoa3AgjnOvGQZtGEdiuKnLIEQ");
        System.out.println(usernameFromToken);
        System.out.println("==================================================");
        String admin = jwtTokenUtil.generateToken("admin");
        System.out.println(admin);
        System.out.println("==================================================");
        String test = jwtTokenUtil.generateToken("test");
        System.out.println(test);
        System.out.println("==================================================");
    }

    @Test
    public void testCreatCollecdp(){
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("isDelete").exists(false)),
                Aggregation.group("source").count().as("num")

        );
        AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(agg,"news" ,HashMap.class);
        ArrayList<HashMap> list = new ArrayList<>();
        list.addAll(aggregationResults.getMappedResults());

        ArrayList<HashMap> docs = new ArrayList<>();
        for (HashMap map : list) {
            HashMap<String, String> doc = new HashMap<>();
            String value = (String) map.get("_id");
            doc.put("name",value );
            mongoTemplate.insert(doc,"dataPermission" );
        }

    }

    @Test
    public void createUser(){
        UserDP user = new UserDP();
        user.setEnabled(true);
        user.setUsername("admin");
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        //添加权限
        HashSet<List<HashMap<String,String>>> auths = new HashSet<>();

        ArrayList<HashMap<String,String>> auth1 = new ArrayList<>();
        HashMap<String, String> map11 = new HashMap<>();
        map11.put("feild", "source");
        map11.put("value","东方网" );
        auth1.add(map11);
        HashMap<String, String> map12 = new HashMap<>();
        map12.put("feild", "editor");
        map12.put("value","杨易颖" );
        auth1.add(map12);

        ArrayList<HashMap<String,String>> auth2 = new ArrayList<>();
        HashMap<String, String> map21 = new HashMap<>();
        map21.put("feild","source" );
        map21.put("value", "海外网");
        auth2.add(map21);
        HashMap<String, String> map22 = new HashMap<>();
        map22.put("feild", "editor");
        map22.put("value","李杭" );
        auth2.add(map22);


        ArrayList<HashMap<String,String>> auth3 = new ArrayList<>();
        HashMap<String, String> map31 = new HashMap<>();
        map31.put("feild","source" );
        map31.put("value", "海外网");
        auth3.add(map31);
        HashMap<String, String> map32 = new HashMap<>();
        map32.put("feild", "editor");
        map32.put("value","责任编辑：乔敬_NN6607" );
        auth3.add(map32);

        auths.add(auth1);
        auths.add(auth2);
        auths.add(auth3);

        user.setAuthorities(auths);
        HashMap<String, Set<String>> feildss = new HashMap<>();
        HashSet<String> feilds = new HashSet<>();
//        feilds.add("fromdb");
        feilds.add("editor");
//        feilds.add("nid");
        feilds.add("time");
        feilds.add("source");
        feilds.add("title");
//        feilds.add("content");
//        feilds.add("url");
        feildss.put("news",feilds );
        user.setCollectionFeilds(feildss);
        mongoTemplate.insert(user,"user_dp" );
    }


    @Test
    public void createUserAdmin(){
        String username = "admin";
        createAndPrintToken(username);

        UserDP user = new UserDP();
        user.setEnabled(true);
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        //添加权限
        HashSet<List<HashMap<String,String>>> auths = new HashSet<>();
        ArrayList<HashMap<String, String>> maps = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("admin","admin" );
        maps.add(map);
        auths.add(maps);
        user.setAuthorities(auths);
        HashMap<String, Set<String>> feildss = new HashMap<>();
        HashSet<String> feilds = new HashSet<>();
        feilds.add("fromdb");
        feilds.add("editor");
        feilds.add("nid");
        feilds.add("time");
        feilds.add("source");
        feilds.add("title");
        feilds.add("content");
        feilds.add("url");
        feildss.put("news",feilds );
        user.setCollectionFeilds(feildss);
        mongoTemplate.insert(user,"user_dp" );
    }

    @Test
    public void createUserTestNull(){
        String username = "testNull";
        createAndPrintToken(username);

        UserDP user = new UserDP();
        user.setEnabled(true);
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        //添加权限
        HashSet<List<HashMap<String,String>>> auths = new HashSet<>();
        user.setAuthorities(auths);
        HashMap<String, Set<String>> feildss = new HashMap<>();
        HashSet<String> feilds = new HashSet<>();
        feilds.add("editor");
        feilds.add("time");
        feilds.add("source");
        feilds.add("title");
        feildss.put("news",feilds );
        user.setCollectionFeilds(feildss);
        mongoTemplate.insert(user,"user_dp" );
    }

    @Test
    public void createUserTestSource(){
        String username = "testSource";
        createAndPrintToken(username);

        UserDP user = new UserDP();
        user.setEnabled(true);
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        //添加权限
        HashSet<List<HashMap<String,String>>> auths = new HashSet<>();

        ArrayList<HashMap<String,String>> auth1 = new ArrayList<>();
        HashMap<String, String> map11 = new HashMap<>();
        map11.put("feild", "source");
        map11.put("value","东方网" );
        auth1.add(map11);

        ArrayList<HashMap<String,String>> auth2 = new ArrayList<>();
        HashMap<String, String> map21 = new HashMap<>();
        map21.put("feild","source" );
        map21.put("value", "海外网");
        auth2.add(map21);

        auths.add(auth1);
        auths.add(auth2);

        user.setAuthorities(auths);
        HashMap<String, Set<String>> feildss = new HashMap<>();
        HashSet<String> feilds = new HashSet<>();
        feilds.add("editor");
        feilds.add("time");
        feilds.add("source");
        feilds.add("title");
        feildss.put("news",feilds );
        user.setCollectionFeilds(feildss);
        mongoTemplate.insert(user,"user_dp" );
    }

    @Test
    public void createUserTestSourceAndEditor(){
        String username = "testSourceAndEditor";
        createAndPrintToken(username);

        UserDP user = new UserDP();
        user.setEnabled(true);
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        //添加权限
        HashSet<List<HashMap<String,String>>> auths = new HashSet<>();

        ArrayList<HashMap<String,String>> auth1 = new ArrayList<>();
        HashMap<String, String> map11 = new HashMap<>();
        map11.put("feild", "source");
        map11.put("value","东方网" );
        auth1.add(map11);
        HashMap<String, String> map12 = new HashMap<>();
        map12.put("feild", "editor");
        map12.put("value","杨易颖" );
        auth1.add(map12);

        ArrayList<HashMap<String,String>> auth2 = new ArrayList<>();
        HashMap<String, String> map21 = new HashMap<>();
        map21.put("feild","source" );
        map21.put("value", "海外网");
        auth2.add(map21);
        HashMap<String, String> map22 = new HashMap<>();
        map22.put("feild", "editor");
        map22.put("value","李杭" );
        auth2.add(map22);


        ArrayList<HashMap<String,String>> auth3 = new ArrayList<>();
        HashMap<String, String> map31 = new HashMap<>();
        map31.put("feild","source" );
        map31.put("value", "海外网");
        auth3.add(map31);
        HashMap<String, String> map32 = new HashMap<>();
        map32.put("feild", "editor");
        map32.put("value","责任编辑：乔敬_NN6607" );
        auth3.add(map32);

        auths.add(auth1);
        auths.add(auth2);
        auths.add(auth3);

        user.setAuthorities(auths);
        HashMap<String, Set<String>> feildss = new HashMap<>();
        HashSet<String> feilds = new HashSet<>();
        feilds.add("editor");
        feilds.add("time");
        feilds.add("source");
        feilds.add("title");
        feildss.put("news",feilds );
        user.setCollectionFeilds(feildss);
        mongoTemplate.insert(user,"user_dp" );
    }

    @Test
    public void createUserTestFeildleast(){
        String username = "testFeildlest";
        createAndPrintToken(username);

        UserDP user = new UserDP();
        user.setEnabled(true);
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        //添加权限
        HashSet<List<HashMap<String,String>>> auths = new HashSet<>();

        ArrayList<HashMap<String,String>> auth1 = new ArrayList<>();
        HashMap<String, String> map11 = new HashMap<>();
        map11.put("feild", "source");
        map11.put("value","东方网" );
        auth1.add(map11);
        HashMap<String, String> map12 = new HashMap<>();
        map12.put("feild", "editor");
        map12.put("value","杨易颖" );
        auth1.add(map12);

        ArrayList<HashMap<String,String>> auth2 = new ArrayList<>();
        HashMap<String, String> map21 = new HashMap<>();
        map21.put("feild","source" );
        map21.put("value", "海外网");
        auth2.add(map21);
        HashMap<String, String> map22 = new HashMap<>();
        map22.put("feild", "editor");
        map22.put("value","李杭" );
        auth2.add(map22);


        ArrayList<HashMap<String,String>> auth3 = new ArrayList<>();
        HashMap<String, String> map31 = new HashMap<>();
        map31.put("feild","source" );
        map31.put("value", "海外网");
        auth3.add(map31);
        HashMap<String, String> map32 = new HashMap<>();
        map32.put("feild", "editor");
        map32.put("value","责任编辑：乔敬_NN6607" );
        auth3.add(map32);

        auths.add(auth1);
        auths.add(auth2);
        auths.add(auth3);

        user.setAuthorities(auths);
        HashMap<String, Set<String>> feildss = new HashMap<>();
        HashSet<String> feilds = new HashSet<>();
//        feilds.add("editor");
        feilds.add("time");
//        feilds.add("source");
        feilds.add("title");
        feildss.put("news",feilds );
        user.setCollectionFeilds(feildss);
        mongoTemplate.insert(user,"user_dp" );
    }

    @Test
    public void createUserTestFeildNotime(){
        String username = "testFeildNotime";
        createAndPrintToken(username);

        UserDP user = new UserDP();
        user.setEnabled(true);
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        //添加权限
        HashSet<List<HashMap<String,String>>> auths = new HashSet<>();

        ArrayList<HashMap<String,String>> auth1 = new ArrayList<>();
        HashMap<String, String> map11 = new HashMap<>();
        map11.put("feild", "source");
        map11.put("value","东方网" );
        auth1.add(map11);
        HashMap<String, String> map12 = new HashMap<>();
        map12.put("feild", "editor");
        map12.put("value","杨易颖" );
        auth1.add(map12);

        ArrayList<HashMap<String,String>> auth2 = new ArrayList<>();
        HashMap<String, String> map21 = new HashMap<>();
        map21.put("feild","source" );
        map21.put("value", "海外网");
        auth2.add(map21);
        HashMap<String, String> map22 = new HashMap<>();
        map22.put("feild", "editor");
        map22.put("value","李杭" );
        auth2.add(map22);


        ArrayList<HashMap<String,String>> auth3 = new ArrayList<>();
        HashMap<String, String> map31 = new HashMap<>();
        map31.put("feild","source" );
        map31.put("value", "海外网");
        auth3.add(map31);
        HashMap<String, String> map32 = new HashMap<>();
        map32.put("feild", "editor");
        map32.put("value","责任编辑：乔敬_NN6607" );
        auth3.add(map32);

        auths.add(auth1);
        auths.add(auth2);
        auths.add(auth3);

        user.setAuthorities(auths);
        HashMap<String, Set<String>> feildss = new HashMap<>();
        HashSet<String> feilds = new HashSet<>();
//        feilds.add("editor");
//        feilds.add("time");
//        feilds.add("source");
        feilds.add("title");
        feildss.put("news",feilds );
        user.setCollectionFeilds(feildss);
        mongoTemplate.insert(user,"user_dp" );
    }

    @Test
    public void createUserTestNoFeilds(){
        String username = "testNoFeilds";
        createAndPrintToken(username);

        UserDP user = new UserDP();
        user.setEnabled(true);
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        //添加权限
        HashSet<List<HashMap<String,String>>> auths = new HashSet<>();

        ArrayList<HashMap<String,String>> auth1 = new ArrayList<>();
        HashMap<String, String> map11 = new HashMap<>();
        map11.put("feild", "source");
        map11.put("value","东方网" );
        auth1.add(map11);
        HashMap<String, String> map12 = new HashMap<>();
        map12.put("feild", "editor");
        map12.put("value","杨易颖" );
        auth1.add(map12);

        ArrayList<HashMap<String,String>> auth2 = new ArrayList<>();
        HashMap<String, String> map21 = new HashMap<>();
        map21.put("feild","source" );
        map21.put("value", "海外网");
        auth2.add(map21);
        HashMap<String, String> map22 = new HashMap<>();
        map22.put("feild", "editor");
        map22.put("value","李杭" );
        auth2.add(map22);


        ArrayList<HashMap<String,String>> auth3 = new ArrayList<>();
        HashMap<String, String> map31 = new HashMap<>();
        map31.put("feild","source" );
        map31.put("value", "海外网");
        auth3.add(map31);
        HashMap<String, String> map32 = new HashMap<>();
        map32.put("feild", "editor");
        map32.put("value","责任编辑：乔敬_NN6607" );
        auth3.add(map32);

        auths.add(auth1);
        auths.add(auth2);
        auths.add(auth3);

        user.setAuthorities(auths);
        HashMap<String, Set<String>> feildss = new HashMap<>();
        HashSet<String> feilds = new HashSet<>();
//        feilds.add("editor");
//        feilds.add("time");
//        feilds.add("source");
//        feilds.add("title");
        feildss.put("news",feilds );
        user.setCollectionFeilds(feildss);
        mongoTemplate.insert(user,"user_dp" );
    }

    @Test
    public void createUsers(){
        createUserAdmin();
        createUserTestNull();
        createUserTestSource();
        createUserTestSourceAndEditor();
        createUserTestFeildNotime();
        createUserTestNoFeilds();
    }

    @Test
    public void showToken(){
        createAndPrintToken("admin");
        createAndPrintToken("testNull");
        createAndPrintToken("testSource");
        createAndPrintToken("testSourceAndEditor");
        createAndPrintToken("testFeildlest");
        createAndPrintToken("testFeildNotime");
        createAndPrintToken("testNoFeilds");
    }

    private void createAndPrintToken(String username) {
        System.out.println("======================================================");
        String token = jwtTokenUtil.generateToken(username);
        System.out.println(username+":token:  "+token);
        System.out.println("======================================================");
    }

}
