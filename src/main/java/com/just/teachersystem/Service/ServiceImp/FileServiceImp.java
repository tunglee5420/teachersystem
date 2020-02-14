package com.just.teachersystem.Service.ServiceImp;



import com.just.teachersystem.Service.FileService;
import com.just.teachersystem.Utill.*;

import com.just.teachersystem.VO.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Component
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:config2.properties")
public class FileServiceImp implements FileService {
    @Autowired
    RedisUtils redisUtils;
    @Value("${file.loginUrl}")
    String loginUrl;
    @Value("${file.username}")
    String username;
    @Value("${file.password}")
    String password;
    @Value("${file.getTokenUrl}")
    String getTokenUrl;

    @Value("${file.comfirmUploadUrl}")
    String confirmUrl;

    @Value("${file.getDownloadToken}")
    String downloadUrl;

    /**
     * 文件云盘登陆
     * @return
     */
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

    /**
     * 请求上传文件的token
     * @param cookie
     * @param fileInfo
     * @param worknum
     * @return
     */
    public HttpClientResult getTgetToken(String cookie,FileInfo fileInfo,String worknum){
        Map<String ,String >header=new HashMap<> ();
        Map<String ,String >body =new HashMap<>();

        header.put("Cookie", cookie);
//        System.out.println(cookie);
        body.put("filename", fileInfo.getFilename());
        body.put("expireTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()+1000*120)));
        body.put("privacy", String.valueOf(false));
        body.put("size", String.valueOf(fileInfo.getSize()));
        body.put("dirPath", "/"+worknum+"/"+Year.now()+"/");
//        System.out.println(body);
        try {
            HttpClientResult h1 = HttpClientUtils.doPost(getTokenUrl,header,body);


            return h1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpClientResult confirmed( String cookie,  String uuid){
        Map<String ,String >header=new HashMap<> ();
        Map<String,String> body =new HashMap<>();
        header.put("Cookie", cookie);
        body.put("matterUuid",uuid);
        try {
            HttpClientResult h1 = HttpClientUtils.doPost(confirmUrl,header,body);
            return h1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpClientResult getDownloadToken(String cookie, String uuid){
        Map<String ,String >header=new HashMap<> ();
        Map<String,String> body =new HashMap<>();

        header.put("Cookie", cookie);
        body.put("matterUuid", uuid);
        body.put("expireTime", new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()+1000*120)));
        try {
            HttpClientResult h1 = HttpClientUtils.doPost(downloadUrl,header,body);

            return h1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
