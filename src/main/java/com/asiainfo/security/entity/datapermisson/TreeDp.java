package com.asiainfo.security.entity.datapermisson;

import lombok.Data;

import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/2211:44
 */
@Data
public class TreeDp {
    private String id;
    private String label;
    private List<TreeDp> children;
}
