package com.just.teachersystem.Controller;

import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.Service.RootService;
import com.just.teachersystem.Utill.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/online/root")
public class RootController {
    @Autowired
    RootService root;

    @PostMapping("/addType")
    public JsonData addType(Kind kind){
//        System.out.println(kind.toString());
        boolean is = root.addType(kind);
        if(is){
            return JsonData.buildSuccess();

        }
        return JsonData.buildError("添加失败");

    }
}
