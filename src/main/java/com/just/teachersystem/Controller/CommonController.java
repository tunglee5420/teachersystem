package com.just.teachersystem.Controller;

import java.util.*;
import com.just.teachersystem.Annotation.Logs;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Service.FileService;
import com.just.teachersystem.Utill.*;
import com.just.teachersystem.VO.FileInfo;
import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/online")
public class CommonController {
    @Autowired
    private CommonService service;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    FileService fileService;
    /**
     * 获取类型列表
     * @return
     */
    @Logs(role = "all",description = "获取类型列表")
    @PostMapping("/getTypeList")
    public JsonData getTypeList(@RequestBody Map<String, String> map){
        String class1=map.get("class1");
//        System.out.println(class1);
        List list= (List) redisUtils.get("class:"+class1);
        if(list==null){
            list=service.getTypeList(class1);
        }
        if(list==null){
            return JsonData.buildError("错误");
        }
        return JsonData.buildSuccess(list);
    }

    /**
     * 获取成就级别
     * @return
     */
    @Logs(role="all",description = "获取成就级别")
    @RequestMapping("/getLevelSet")
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

    /**
     * 获取奖项内容
     * @return
     */
    @Logs(role="all",description = "获取奖项内容")
    @RequestMapping("/getPrizeSet")
    public JsonData getPrizeSet(){
        Set set= (Set) redisUtils.get("class:prize");
        if(set==null){
            set= service.getPrizelSet();
        }

        if(set==null) {
            return JsonData.buildError("错误");
        }
        return JsonData.buildSuccess(set);
    }

    /**
     * 获取部门列表
     * @return
     */
    @Logs(role="all",description = "获取部门列表")
    @RequestMapping("/getDepartmentList")
    public JsonData getDepartmentList(){
        List list= (List) redisUtils.get("department");
        if(list==null){
            list=service.getDepartmentList();
        }
        if(list==null) {
            return JsonData.buildError("错误");
        }
        return JsonData.buildSuccess(list);
    }

    /**
     * 获得上传文件的token
     * @param fileInfo
     * @param headers
     * @return
     */
    @Logs(role="all",description = "获得上传文件的token")
    @PostMapping("/getPanToken")
    public JsonData getPanToken(@RequestBody FileInfo fileInfo ,@RequestHeader Map<String ,String>headers){
        Claims claims =JwtUtils.checkJWT(headers.get("token"));
        String worknum = (String) claims.get("worknum");
        Map map= (Map) redisUtils.get("file:login");


        if(map==null){
            map= (Map) fileService.filePanLogin().getContent();
        }
        String cookie= (String) map.get("cookie");
//
        try {
            HttpClientResult h=fileService.getTgetToken(cookie,fileInfo,worknum);
            if (h!=null&&h.getCode()==200){
                return JsonData.buildSuccess(h.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonData.buildError("请求出错");
    }

    /**
     * 证实文件上传状况
     * @param jsons
     * @return
     */
    @Logs(role="all",description = "证实文件上传状况")
    @PostMapping("/confirmUploaded")
    public JsonData confirmUploaded (@RequestBody Map<String, String> jsons){

        String uuid=jsons.get("uuid");
        Map map= (Map) redisUtils.get("file:login");
        if(map==null){
            map= (Map) fileService.filePanLogin().getContent();
        }
        String cookie= (String) map.get("cookie");

        HttpClientResult h=fileService.confirmed(cookie, uuid);
        if(h != null && h.getCode()==200){
            return  JsonData.buildSuccess(h.getContent());
        }
        return JsonData.buildError(h.getContent().toString());
    }

    /**
     * 获取下载 Token
     * @param jsons
     * @return
     */
    @Logs(role="all",description = "获取下载 Token")
    @PostMapping("/getDownloadToken")
    public JsonData getDownloadToken(@RequestBody Map<String, String> jsons){
        String uuid=jsons.get("uuid");
        Map map= (Map) redisUtils.get("file:login");
        if(map==null){
            map= (Map) fileService.filePanLogin().getContent();
        }
        String cookie= (String) map.get("cookie");

        HttpClientResult h=fileService.getDownloadToken(cookie, uuid);
        if(h != null && h.getCode()==200){
            return  JsonData.buildSuccess(h.getContent());
        }
        return JsonData.buildError(h.getContent().toString());
    }

    /**
     * 验证工号和token的一致性
     * @param headers
     * @param jsons
     * @return
     */
    @Logs(role="all",description = "验证工号和token的一致性")
    @PostMapping("/validate")
    public JsonData validate(@RequestHeader Map<String ,String>headers, @RequestBody Map<String, String> jsons){
        Claims claims =JwtUtils.checkJWT(headers.get("token"));
        String worknum = (String) claims.get("worknum");
        String worknum1=jsons.get("worknum");
        if(worknum1==null) return JsonData.buildError("工号为空");
        if(worknum.equals(worknum1)){
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("验证错误");
    }



}
