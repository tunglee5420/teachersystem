package com.just.teachersystem.Controller;

import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/online/officeAdmin")
public class OfficeAdminController {
    @Autowired
    CommonService service;
    @PostMapping("/update")
    public JsonData updateUserInfo( UserInfo userInfo){

        boolean res=service.updateUserInfo(userInfo);
        return JsonData.buildSuccess(res);
    }

}
