package com.just.teachersystem;
import java.time.Year;
import java.util.Calendar;
import	java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import	java.util.Map;


import com.just.teachersystem.Entity.UserLog;
import com.just.teachersystem.Mapper.UserLogMapper;
import com.just.teachersystem.Service.FileService;
import com.just.teachersystem.Service.ServiceImp.FileServiceImp;
import com.just.teachersystem.Utill.HttpClientResult;
import com.just.teachersystem.Utill.HttpClientUtils;
import com.just.teachersystem.Utill.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeachersystemApplicationTests {

    @Autowired
    FileService fileService;
    @Autowired
    UserLogMapper logMapper;
    @Test
    public void contextLoads() {
        HttpClientResult h=fileService.confirmed("_ak=7136904b-40ad-400c-5564-1f5de5eb9a61; Path=/; Expires=Wed, 06 Nov 2019 07:12:23 GMT","a2657677-18f1-46a0-5fd0-8b0b050c3dcb");

        System.out.println(h);
    }
    @Test
    public void Test1(){
        UserLog u1=new UserLog();
        u1.setWorknum("5163232032");
        u1.setRole("科室管理员");

        int i=logMapper.insertUserLog(u1);
        if(i==0){
            System.out.println("错误");

        }
        System.out.println("插入成功");
    }
    @Test
    public void Test2() throws Exception {
        String url ="http://test.iskye.cn/api/alien/fetch/upload/token";
        Map<String, String  >body=new HashMap<> ();
        body.put("filename", "name.jpg");
        body.put("expireTime", "2019-09-30 23:59:59");
        body.put("privacy", String.valueOf(false));
        body.put("size", String.valueOf(125645));
        body.put("dirPath", "/home/");
        String cookie="_ak=410dfa4a-63fd-4ec6-5eba-58f6ba4a381e; Path=/; Expires=Wed, 30 Oct 2019 02:01:46 GMT";
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", cookie);
//
        System.out.println(HttpClientUtils.doPost(url, header, body));


    }


}
