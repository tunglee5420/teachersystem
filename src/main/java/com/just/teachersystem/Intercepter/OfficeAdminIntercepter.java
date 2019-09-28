package com.just.teachersystem.Intercepter;

import com.alibaba.fastjson.JSON;

import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class OfficeAdminIntercepter  extends OnlineIntercepter {


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

        if(JwtUtils.checkJWT(token)!=null){
            if((int)JwtUtils.checkJWT(token).get("permission")==2){
                return true;
            }
            printJson(response,-1,"你没有操作权限");
        }
        printJson(response,-1,"token过期,请重新登陆");
        return false;
    }




}
