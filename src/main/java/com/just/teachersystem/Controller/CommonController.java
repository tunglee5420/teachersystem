package com.just.teachersystem.Controller;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;

import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Service.FileService;
import com.just.teachersystem.Utill.*;
import com.just.teachersystem.VO.FileInfo;
import io.jsonwebtoken.Claims;
import org.apache.ibatis.annotations.Param;
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
    @PostMapping("/getTypeList")
    public JsonData getTypeList(@RequestBody Map<String, String> map){
        String class1=map.get("class1");
        System.out.println(class1);
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
     * 获取部门列表
     * @return
     */
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
    @PostMapping("/getPanToken")
    public JsonData getPanToken(@RequestBody FileInfo fileInfo ,@RequestHeader Map<String ,String>headers){
        Claims claims =JwtUtils.checkJWT(headers.get("token"));
        String worknum = (String) claims.get("worknum");
        Map map= (Map) redisUtils.get("file:login");
        System.out.println(fileInfo);
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




}
