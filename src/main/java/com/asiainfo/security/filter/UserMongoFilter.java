package com.asiainfo.security.filter;

import com.asiainfo.security.entity.datapermisson.UserDP;
import com.asiainfo.security.service.UserMongoService;
import com.asiainfo.security.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author Mr.LkZ
 * @version 2019/10/1716:17
 */
@Slf4j
//@Component
public class UserMongoFilter implements Filter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserMongoService userMongoService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        final String requestHeader = req.getHeader("Authorization");

        String username = null;
        String key = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            key = requestHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(key);
                log.info("username="+username);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        UserDP user = userMongoService.findUserDpByName(username);
        List<Object> dataRole = (List<Object>) user.getDataRoles();
        if (dataRole==null)throw  new RuntimeException("没有管理员权限...");
        if (dataRole.contains("admin")){
            doFilter(servletRequest,servletResponse ,filterChain );
        }else {
            throw  new RuntimeException("没有管理员权限...");
        }
    }
}
