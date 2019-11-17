package com.just.teachersystem.Annotation;

import com.just.teachersystem.Utill.JwtUtils;
import org.aspectj.lang.reflect.MethodSignature;
import com.just.teachersystem.Entity.UserLog;
import com.just.teachersystem.Service.CommonService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class UserLogAspect {

    @Autowired
    private CommonService commonService;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(com.just.teachersystem.Annotation.Logs)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
//        System.out.println("切面。。。。。");
        //保存日志
        UserLog log=new UserLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
        Logs myLog = method.getAnnotation(Logs.class);
        if (myLog != null) {
            String role = myLog.role();
            String discription=myLog.description();
            log.setRole(role);//保存获取的操作
            log.setDescription(discription);
        }


        //获取用户ip地址和url
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURL = request.getRequestURL().toString();
        String token=request.getHeader("token");
        String ip = request.getRemoteAddr();
//        System.out.println(token);
        if(token!=null) {
            log.setWorknum((String) JwtUtils.checkJWT(token).get("worknum"));
        }
//        System.out.println(ip);
//        System.out.println(requestURL);
        log.setIp(ip);
        log.setUrl(requestURL);
        //调用service保存SysLog实体类到数据库
        commonService.insertUserLog(log);
    }

}
