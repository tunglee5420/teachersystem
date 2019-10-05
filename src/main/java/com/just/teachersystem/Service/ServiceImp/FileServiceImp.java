package com.just.teachersystem.Service.ServiceImp;
import	java.lang.annotation.Retention;

import com.just.teachersystem.Service.FileService;
import com.just.teachersystem.Utill.HttpClientResult;
import com.just.teachersystem.Utill.HttpClientUtils;

import com.just.teachersystem.Utill.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Component

@PropertySource("classpath:config.properties")
public class FileServiceImp implements FileService {
    @Autowired
    RedisUtils redisUtils;
    @Value("${file.loginUrl}")
    String loginUrl;
    @Value("${file.username}")
    String username;
    @Value("${file.password}")
    String password;

    public HttpClientResult filePanLogin(){
        Map<String, String>map=new HashMap<>();
        map.put("username",this.username);
        map.put("password",this.password);
        try {
            HttpClientResult h1=HttpClientUtils.doPost(this.loginUrl,map);
            if(h1.getContent()!=null){
                redisUtils.set("file:login",h1.getContent(),60*60*24*30);
            }
            return h1;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

}
