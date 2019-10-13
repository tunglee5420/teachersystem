package com.just.teachersystem.Controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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

    /**
     * 修改密码前验证身份
     * @param header
     * @param password
     * @return
     */

    @PostMapping("/check")
    public JsonData check(@RequestHeader Map<String ,String> header,@RequestParam("password") String password){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        boolean res=userService.check(worknum, password);
        if(res){
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("验证错误");
    }

    /**
     * 需改密码
     * @param header
     * @param password
     * @return
     */
    @PostMapping("/updatePassword")
    public JsonData updatePassword(@RequestHeader Map<String ,String> header,@RequestParam("password") String password){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        UserInfo userInfo=new UserInfo();
        userInfo.setWorknum(worknum);
        userInfo.setPassword( EncryptUtil.getInstance().MD5(password));
        boolean res=rootService.updateUserInfo(userInfo);
        if(res){
            redisUtils.del("login:"+worknum);//更换密码后需要重新登陆，删除缓存中的数据
            return JsonData.buildSuccess();
        }
        return JsonData.buildError("修改失败");
    }

    /**
     * 提交建设类信息
     * @param info
     * @return
     */
    @PostMapping("/addConstruction")
    public JsonData addConstruction(@RequestHeader Map<String ,String> header,ConstructionInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        info.setWorknum(worknum);
        info.setClass1("建设类");
//        info.setDepartment("网络信息中心");
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
    @PostMapping("/updateUserConstruction")
    public JsonData updateUserConstruction(@RequestHeader Map<String ,String> header,ConstructionInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");


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
    @PostMapping("/getMyConstructionInfo")
    public JsonData  getMyConstructionInfo(@RequestHeader Map<String ,String> header,
                                           @RequestParam(value = "page",defaultValue = "1") int page,
                                           @RequestParam(value = "size",defaultValue = "30")int size) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        List list=userService.getMyConstructionInfo(worknum);
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
    @PostMapping("/addAchievement")
    public JsonData addAchievement(@RequestHeader Map<String ,String> header,AchievementInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
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
    @PostMapping("/getMyAchievementInfo")
    public JsonData  getMyAchievementInfo(@RequestHeader Map<String ,String> header,
                                          @RequestParam(value = "page",defaultValue = "1") int page,
                                          @RequestParam(value = "size",defaultValue = "30")int size) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        List list=userService.getMyAchievementInfo(worknum);
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
    @PostMapping("/updateUserAchievement")
    public JsonData updateUserAchievement(@RequestHeader Map<String ,String> header,AchievementInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        info.setWorknum(worknum);
        boolean res = commonService.updateAchievementServ(info);
        if(res){
            return  JsonData.buildSuccess("修改成功");
        }
        return JsonData.buildError("修改失败");

    }

    @PostMapping("/addAward")
    public JsonData addAward(@RequestHeader Map<String ,String> header,AwardInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
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
    @PostMapping("/getMyAwardInfo")
    public JsonData  getMyAwardInfo(@RequestHeader Map<String ,String> header,
                                    @RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "size",defaultValue = "30")int size) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        List list=userService.getMyAwardInfo(worknum);

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
    @PostMapping("/updateUserAward")
    public JsonData updateUserAward(@RequestHeader Map<String ,String> header,AwardInfo info){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String worknum=(String) claims.get("worknum");
        info.setWorknum(worknum);
        boolean res = commonService.updateAwardServ(info);
        if(res){
            return  JsonData.buildSuccess("修改成功");
        }
        return JsonData.buildError("修改失败");

    }


}
