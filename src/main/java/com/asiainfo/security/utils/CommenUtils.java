package com.asiainfo.security.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author Mr.LkZ
 * @version 2019/10/239:15
 */
public class CommenUtils {
    private CommenUtils(){}
    public static void addPageCriteria(Pageable pageable, Query query, int num, int pageSize) {
        int start = (num - 1) * pageSize;
        query.skip(start);
        query.limit(pageSize);
        //设置排序
        Sort sort = pageable.getSort();
        query.with(sort);
    }
}
