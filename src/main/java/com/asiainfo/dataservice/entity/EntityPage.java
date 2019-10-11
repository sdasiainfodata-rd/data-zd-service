package com.asiainfo.dataservice.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/1114:37
 */
@Data
public class EntityPage {
    private List<HashMap> content;
    private long totalElements;
    private int size;
    private int page;
}
