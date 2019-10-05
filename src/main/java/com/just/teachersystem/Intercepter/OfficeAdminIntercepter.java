package com.just.teachersystem.Intercepter;


import com.just.teachersystem.Utill.JwtUtils;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
            int permission =(int)JwtUtils.checkJWT(token).get("permission");
            if(permission==2||permission ==3){
                return true;
            }
            printJson(response,-1,"你没有操作权限");
        }
        printJson(response,-1,"token过期,请重新登陆");
        return false;
    }




}
