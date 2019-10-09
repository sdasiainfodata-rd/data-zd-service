package com.asiainfo.security.entity;

import lombok.Data;

import java.util.*;

/**
 * 封装user的实体类
 * @author Mr.LkZ
 * @version 2019/10/817:16
 */
@Data
public class User {
    private HashMap user;
    private String token;
    private List<HashMap> roles ;
    private Set<String> api_permitions ;
    private Set<String> meanu ;
}
