package com.just.teachersystem.Controller;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;

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

    /**
     * 获取部门列表
     * @return
     */
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

    @PostMapping("/getPanToken")
    public JsonData getPanToken(FileInfo fileInfo ,@RequestHeader Map<String ,String>headers){
        String url ="http://test.iskye.cn/api/alien/fetch/upload/token";
        Claims claims =JwtUtils.checkJWT(headers.get("token"));
        String worknum = (String) claims.get("worknum");
        Map map= (Map) redisUtils.get("file:login");
        if(map==null){
            map= (Map) fileService.filePanLogin().getContent();
        }
        String cookie= (String) map.get("cookie");
        Map<String ,String >header=new HashMap<> ();
        Map<String,String> body =new HashMap<>();

        header.put("Cookie", cookie);
        body.put("filename", fileInfo.getFilename());
        body.put("expireTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()+1000*120)));
        body.put("privacy", String.valueOf(false));
        body.put("size", String.valueOf(fileInfo.getSize()));
        body.put("dirPath", "/"+worknum+"/"+Year.now()+"/");

        try {
            HttpClientResult h=HttpClientUtils.doPost(url, header, body);
            if (h.getCode()==200){
                return JsonData.buildSuccess(h.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonData.buildError("请求出错");
    }
}
