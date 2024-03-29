package com.asiainfo.security.filter;

import com.asiainfo.security.mapper.UserMapper;
import com.asiainfo.security.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void init(FilterConfig filterConfig)  {

    }

    /**
     *用户权限过滤器,匹配uri中是否包含权限
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        String key = servletRequest.getParameter("KEY");
        final String requestHeader = req.getHeader("Authorization");

        String username = null;
        String key = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            key = requestHeader.substring(7);
            log.info("token="+key);
            try {
                username = jwtTokenUtil.getUsernameFromToken(key);
                log.info("username="+username);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }


        //没有key,拒绝用户访问
        if (StringUtils.isEmpty(key)){
            toRefusePage(req,servletResponse);
            return;
        }
        try {
            username = jwtTokenUtil.getUsernameFromToken(key);
        }catch (Exception e){
            toRefusePage(req,servletResponse );
        }

        //获取urls,若权限urls为空,拒绝用户访问
        if (StringUtils.isEmpty(username)){
            toRefusePage(req,servletResponse);
            return;
        }
        List<String> urls = userMapper.findAllUrl(username.toString());
        if (urls == null){
            toRefusePage(req,servletResponse);
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

            if (requestURI != null && url!=null&& requestURI.contains(url)) {
                req.getRequestDispatcher(requestURI).forward(servletRequest, servletResponse);
                return;
            }
        }

        //没有权限,拒绝访问
        toRefusePage(req,servletResponse);
    }

    private void toRefusePage(HttpServletRequest req,ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
//        resp.setStatus(401);
        req.getRequestDispatcher("/refuse").forward(req,resp );
//        resp.sendRedirect("http://localhost:8013/401");
    }

    @Override
    public void destroy() {

    }
}
