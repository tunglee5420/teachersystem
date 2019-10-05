package com.just.teachersystem.Intercepter;


import com.just.teachersystem.Utill.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CollegeAdminIntercepter extends OnlineIntercepter {
    /**
     * 进入controller 之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token= request.getHeader("token");
        int permission =(int)JwtUtils.checkJWT(token).get("permission");
        if(JwtUtils.checkJWT(token)!=null){
            if(permission==1&& permission ==3){
                return true;
            }
            printJson(response,-1,"你没有操作权限");
        }
        printJson(response,-1,"token过期,请重新登陆");
        return false;


    }

    /**
     * 调用controller之后，视图渲染之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 完成之后，用于资源清理
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
