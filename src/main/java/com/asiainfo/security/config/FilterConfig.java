package com.asiainfo.security.config;

import com.asiainfo.security.filter.MyAuthorizedFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.LkZ
 * @version 2019/9/303:50
 */
@Configuration
public class FilterConfig {
    @Autowired
    private MyAuthorizedFilter myAuthorizedFilter;

    /**
     * 将过滤器myAuthorizedFilter注册并设置拦截规则
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean registerAuthFilter2() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(myAuthorizedFilter);
        registration.addUrlPatterns("/api/*");
        registration.setName("myAuthorizedFilterOther");
        registration.setOrder(2);  //值越小，Filter越靠前。
        return registration;
    }
    //如果有多个Filter，再写一个public FilterRegistrationBean registerOtherFilter(){...}即可。
}
