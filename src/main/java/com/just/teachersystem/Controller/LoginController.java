package com.just.teachersystem.Controller;


import com.just.teachersystem.Entity.User;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/outline")
public class LoginController {
    @Autowired
    private CommonService service;
    /**
     * 登陆
     * @param
     * @param
     * @return
     */
    @PostMapping(value = "/login" , produces = "application/json;charset=utf-8")
    public JsonData login(@RequestBody  @Validated  User user){
        String worknum=user.getWorknum();
        String password=user.getPassword();
        long permission =user.getPermission();
        if(worknum==null||password==null) {
            return JsonData.buildError("参数异常");
        }
        EncryptUtil encryptUtil=EncryptUtil.getInstance();
        Map<String, Object> res=service.login(worknum,encryptUtil.MD5(password));
        if(res==null){
            return JsonData.buildError("密码错误或者账户不存在");
        }
        if(permission==1&&(Integer)res.get("permission")<1){
           return JsonData.buildError("权限不足，请从普通用户入口登陆");
        }
        return JsonData.buildSuccess(res);
//        return null;
    }
}
