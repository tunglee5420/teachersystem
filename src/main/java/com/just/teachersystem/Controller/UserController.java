package com.just.teachersystem.Controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.just.teachersystem.Annotation.Logs;
import com.just.teachersystem.Entity.Achievement;
import com.just.teachersystem.Service.CollegeAdminService;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Service.RootService;
import com.just.teachersystem.Service.UserService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.Utill.RedisUtils;
import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.UserInfo;
import io.jsonwebtoken.Claims;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/online/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RootService rootService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    CommonService commonService;
    @Autowired
    CollegeAdminService collegeAdminService;

    /**
     * 修改密码前验证身份
     * @param header
     * @param map
     * @return
     */

    @Logs(role = "all",description = "验证身份和密码")
    @PostMapping("/check")
    public JsonData check(@RequestHeader Map<String ,String> header,@RequestBody Map<String, String> map){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        String password=map.get("password");
        System.out.println(password);
        boolean res=userService.check(worknum, password);
        if(res){
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("验证错误");
    }

    /**
     * 需改密码
     * @param header
     * @param map
     * @return
     */
    @Logs(role = "all",description = "修改密码")
    @PostMapping("/updatePassword")
    public JsonData updatePassword(@RequestHeader Map<String ,String> header,@RequestBody Map<String, String> map){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        UserInfo userInfo=new UserInfo();
        userInfo.setWorknum(worknum);
        String password=map.get("newPassword");
        System.out.println(password);
        userInfo.setPassword( password);
        boolean res=rootService.updateUserInfo(userInfo);
        if(res){
            redisUtils.del("login:"+worknum);//更换密码后需要重新登陆，删除缓存中的数据
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("修改失败");
    }

    /**
     * 获取个人信息
     * @param header
     * @return
     */
    @Logs(role = "all",description = "获取个人信息")
    @PostMapping("/getMyInfo")
    public JsonData getMyInfo(@RequestHeader Map<String, String> header) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        UserInfo userInfo = userService.getUserInfo(worknum);
        if(userInfo==null){
            return JsonData.buildError("个人信息获取失败");
        }
        return JsonData.buildSuccess(userInfo);
    }


    /**
     * 提交建设类信息
     * @param info
     * @return
     */
    @Logs(role = "user",description = "提交建设类信息")
    @PostMapping(value = "/addConstruction",produces = "application/json;charset=utf-8")
    public JsonData addConstruction(@RequestHeader Map<String ,String> header,@RequestBody ConstructionInfo info){

        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        System.out.println(info);
        if(!info.getWorknum().equals(worknum)){
            return JsonData.buildError("输入工号与当前工号不符合，请录入本人账号信息");
        }
        info.setWorknum(worknum);
        info.setClass1("建设类");
        int res=userService.addConstruction(info);
        if(res>0){
            return JsonData.buildSuccess("提交成功");
        }
        return JsonData.buildError("提交失败");
    }

    /**
     * 用户更新建设类信息
     * @param header
     * @param info
     * @return
     */
    @Logs(role = "user",description = "更新建设类信息")
    @PostMapping("/updateUserConstruction")
    public JsonData updateUserConstruction(@RequestHeader Map<String ,String> header,@RequestBody ConstructionInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        if(!info.getWorknum().equals(worknum)){
            return JsonData.buildError("输入工号与当前工号不符合，请录入本人账号信息");
        }

        info.setWorknum(worknum);
        info.setClass1("建设类");
        boolean res = commonService.updateConstructionServ(info);
        if(res){
            return  JsonData.buildSuccess("修改成功");
        }
        return JsonData.buildError("修改失败");

    }

    /**
     * 获取用户建设类信息
     * @param header
     * @return
     */
    @Logs(role = "user",description = "查看个人建设类信息")
    @PostMapping("/getMyConstructions")
    public JsonData  getMyConstructions(@RequestHeader Map<String ,String> header,
                                           @RequestBody Map<String, String> map,
                                            @RequestParam(value = "page") int page,
                                            @RequestParam(value = "size")int size) {

        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");

        ConstructionInfo construction=new ConstructionInfo();
        construction.setStatus(Integer.parseInt(map.get("status")));
        construction.setWorknum(worknum);
        construction.setSchoolyear(map.get("schoolyear"));
        construction.setYear(map.get("year"));
        List list=userService.getMyConstructions(construction);
        if(list==null){
            return JsonData.buildSuccess("暂无数据");
        }
        PageHelper.startPage(page,size);
        PageInfo<AchievementInfo> pageInfo=new PageInfo<> (list);
        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 添加成果类的信息
     * @param header
     * @param info
     * @return
     */
    @Logs(role = "user",description = "添加成果类的信息")
    @PostMapping("/addAchievement")
    public JsonData addAchievement(@RequestHeader Map<String ,String> header,@RequestBody AchievementInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        if(!info.getWorknum().equals(worknum)){
            return JsonData.buildError("输入工号与当前工号不符合，请录入本人账号信息");
        }
        info.setWorknum(worknum);
        boolean res=userService.addAchievement(info);
        if (res){
            return JsonData.buildSuccess("添加成功");
        }
        return JsonData.   buildError("添加失败");
    }

    /**
     * 获取用户成果类信息
     * @param header
     * @return
     */
    @Logs(role = "user",description = "用户查看成果类信息")
    @PostMapping("/getMyAchievements")
    public JsonData  getMyAchievements(@RequestHeader Map<String ,String> header,
                                       @RequestBody Map<String, String> map,
                                       @RequestParam(value = "page") int page,
                                       @RequestParam(value = "size")int size) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        AchievementInfo achievement= new AchievementInfo();
        achievement.setWorknum(worknum);
        achievement.setStatus(Integer.parseInt(map.get("status")));
        achievement.setSchoolYear(map.get("schoolyear"));
        achievement.setYear(map.get("year"));
        List list=userService.getMyAchievements(achievement);
        if(list==null){
            return JsonData.buildSuccess("暂无数据");
        }
        PageHelper.startPage(page,size);
        PageInfo<AchievementInfo> pageInfo=new PageInfo<> (list);
        return JsonData.buildSuccess(pageInfo);
    }


    /**
     * 用户更新成果类类信息
     * @param header
     * @param info
     * @return
     */
    @Logs(role = "user",description = "用户更新成果类类信息")
    @PostMapping("/updateUserAchievement")
    public JsonData updateUserAchievement(@RequestHeader Map<String ,String> header,@RequestBody AchievementInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        if(!info.getWorknum().equals(worknum)){
            return JsonData.buildError("输入工号与当前工号不符合，请录入本人账号信息");
        }
        info.setWorknum(worknum);
        boolean res = commonService.updateAchievementServ(info);
        if(res){
            return  JsonData.buildSuccess("修改成功");
        }
        return JsonData.buildError("修改失败");

    }

    /**
     * 添加获奖类类信息
     * @param header
     * @param info
     * @return
     */
    @Logs(role = "user",description = "用户添加获奖类类信息")
    @PostMapping("/addAward")
    public JsonData addAward(@RequestHeader Map<String ,String> header,@RequestBody AwardInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");

        if(!info.getWorknum().equals(worknum)){
            return JsonData.buildError("输入工号与当前工号不符合，请录入本人账号信息");
        }
        info.setWorknum(worknum);
        boolean res = userService.addAward(info);
        if (res){
            return JsonData.buildSuccess("添加成功");
        }
        return JsonData.buildError("添加失败");
    }

    /**
     * 获取用户提交的获奖类信息
     * @param header
     * @return
     */
    @Logs(role = "user",description = "用户添加获奖类类信息")
    @PostMapping("/getMyAwards")
    public JsonData  getMyAwards(@RequestHeader Map<String ,String> header,
                                    @RequestBody Map<String, String> map,
                                    @RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "size",defaultValue = "30")int size) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");

        AwardInfo award=new AwardInfo();
        award.setSchoolYear(map.get("schoolyear"));
        award.setYear(map.get("year"));
        award.setStatus(Integer.parseInt(map.get("status")));
        List list=userService.getMyAwards(award);

        if(list==null){
            return JsonData.buildSuccess("暂无数据");
        }
        PageHelper.startPage(page,size);
        PageInfo<AwardInfo> pageInfo=new PageInfo<> (list);

        return JsonData.buildSuccess(pageInfo);
    }


    /**
     * 用户更新获奖类信息
     * @param header
     * @param info
     * @return
     */
    @Logs(role = "user",description = "用户更新获奖类信息")
    @PostMapping("/updateUserAward")
    public JsonData updateUserAward(@RequestHeader Map<String ,String> header,@RequestBody AwardInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        if(!info.getWorknum().equals(worknum)){
            return JsonData.buildError("输入工号与当前工号不符合，请录入本人账号信息");
        }
        info.setWorknum(worknum);
        boolean res = commonService.updateAwardServ(info);
        if(res){
            return  JsonData.buildSuccess("修改成功");
        }
        return JsonData.buildError("修改失败");

    }

    /**
     * 获取入口开放权限
     * @return
     */
    @Logs(role = "user",description = "用户获取入口开放权限")
    @PostMapping("/getEntrancePermission")
    public JsonData getEntrancePermission() {
        Map<Object,Object> map=redisUtils.hmget("Entrance:user");
        if (map==null ||map.isEmpty()){
            return JsonData.buildError("获取出错");
        }
        return JsonData.buildSuccess(map);

    }



}
