package com.just.teachersystem.Controller;
import	java.util.Map;
import java.util.Set;

import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/online")
public class CommonController {
    @Autowired
    private CommonService service;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 获取类型列表
     * @return
     */
    @PostMapping("/getTypeList")
    public JsonData getTypeList(){
        System.out.println(111);
        Map map= (Map) redisUtils.get("class:class");
        if(map==null){

            map=service.getTypeList();
        }
        if(map==null){
            return JsonData.buildError("错误");
        }
        return JsonData.buildSuccess(map);
    }

    /**
     * 获取成就级别
     * @return
     */
    @GetMapping("/getLevelSet")
    public JsonData getLevelSet(){
        Set set= (Set) redisUtils.get("class:level");
        if(set==null){
            set= service.getLevelSet();
        }

        if(set==null) {
            return JsonData.buildError("错误");
        }
        return JsonData.buildSuccess(set);
    }

    @GetMapping("/getDepartmentList")

    public JsonData getDepartmentList(){
        Map map= (Map) redisUtils.get("department");
        if(map==null){
            map=service.getDepartmentList();
        }

        if(map==null) {
            return JsonData.buildError("错误");
        }
        return JsonData.buildSuccess(map);
    }
}
