package com.asiainfo.security.filter;

import com.asiainfo.security.mapper.UserMapper;
import com.asiainfo.security.utils.JwtHelper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/9/306:13
 */

@Slf4j
@Component
public class MyAuthorizedFilter implements Filter {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void init(FilterConfig filterConfig)  {

    }

    /**
     *用户权限过滤器,匹配uri中是否包含权限
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String key = servletRequest.getParameter("KEY");
        Claims claims = JwtHelper.parseJWT(key);
        Object username = claims.get("user_name");
        //没有key,拒绝用户访问
        if (StringUtils.isEmpty(key)){
            req.getRequestDispatcher("/refuse.html").forward(servletRequest,servletResponse );
            return;
        }
        //获取urls,若权限urls为空,拒绝用户访问
        if (StringUtils.isEmpty(username)){
            req.getRequestDispatcher("/refuse.html").forward(servletRequest,servletResponse );
            return;
        }
        List<String> urls = userMapper.findAllUrl(username.toString());
        if (urls == null){
            req.getRequestDispatcher("/refuse.html").forward(servletRequest,servletResponse );
            return;
        }
        String requestURI = req.getRequestURI();
        //输出uri和权限
        log.info(username+"uri:"+requestURI);
        for (String url : urls) {
            log.info("authority:"+url);
        }
        //遍历权限,uri包含权限者转发不含key的uri
        for (String url : urls) {
            if (requestURI != null && requestURI.contains(url)) {
                req.getRequestDispatcher(requestURI).forward(servletRequest, servletResponse);
                return;
            }
        }

        //没有权限,拒绝访问
        req.getRequestDispatcher("/refuse.html").forward(servletRequest,servletResponse );
    }

    @Override
    public void destroy() {

    }
}
