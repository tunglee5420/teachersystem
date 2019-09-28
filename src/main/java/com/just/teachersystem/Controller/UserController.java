package com.just.teachersystem.Controller;

import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Service.UserService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.VO.UserInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/online/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CommonService commonService;

    /**
     * 修改密码前验证身份
     * @param header
     * @param password
     * @return
     */

    @PostMapping("/check")
    public JsonData check(@RequestHeader Map<String ,String> header,@RequestParam("password") String password){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        boolean res=userService.check(worknum, password);
        if(res){
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("验证错误");
    }

    /**
     * 需改密码
     * @param header
     * @param password
     * @return
     */
    @PostMapping("/updatePassword")
    public JsonData updatePassword(@RequestHeader Map<String ,String> header,@RequestParam("password") String password){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        UserInfo userInfo=new UserInfo();
        userInfo.setWorknum(worknum);
        userInfo.setPassword( EncryptUtil.getInstance().MD5(password));
        boolean res=commonService.updateUserInfo(userInfo);
        if(res){
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("修改失败");
    }
}
