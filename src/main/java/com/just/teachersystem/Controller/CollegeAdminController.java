package com.just.teachersystem.Controller;

import com.just.teachersystem.Service.CollegeAdminService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.VO.UserInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/online/collegeAdmin")
public class CollegeAdminController {
    @Autowired
    CollegeAdminService collegeAdminService;

    @PostMapping("/getDptUserInfo")
    public JsonData getDptUserInfo(@RequestHeader Map<String ,String> header, @RequestParam("worknum") String worknum , @RequestParam("name") String name){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");

        UserInfo userInfo = new UserInfo();
        userInfo.setDptname(department);
        if (worknum!=null) userInfo.setWorknum(worknum);
        if(name != null) userInfo.setName(name);


        List list=collegeAdminService.getUserInfo(userInfo);


        return JsonData.buildSuccess(list);
    }

}
