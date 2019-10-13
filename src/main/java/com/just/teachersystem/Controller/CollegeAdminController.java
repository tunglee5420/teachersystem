package com.just.teachersystem.Controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.teachersystem.Service.CollegeAdminService;
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
@RequestMapping("/api/online/collegeAdmin")
public class CollegeAdminController {
    @Autowired
    CollegeAdminService collegeAdminService;
    @Autowired
    RedisUtils redisUtils;

    /**
     * 获取部门用户
     * @param header
     * @param worknum
     * @param name
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getDptUserInfo")
    public JsonData getDptUserInfo(@RequestHeader Map<String ,String> header,
                                   @RequestParam("worknum") String worknum ,
                                   @RequestParam("name") String name,
                                   @RequestParam(value = "page",defaultValue = "1") int page,
                                   @RequestParam(value = "size",defaultValue = "30")int size
                                   ){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");

        UserInfo userInfo = new UserInfo();
        userInfo.setDptname(department);
        if (worknum!=null) userInfo.setWorknum(worknum);
        if(name != null) userInfo.setName(name);

        PageHelper.startPage(page,size);
        List list=collegeAdminService.getUserInfo(userInfo);
        PageInfo<UserInfo> pageInfo = new PageInfo<> (list);
        return JsonData.buildSuccess(pageInfo);
    }

    /**
     * 修改部门成员密码
     * @param headers
     * @param worknum
     * @param password
     * @return
     */
    @PostMapping("/updateUserPassword")
    public JsonData updateUserPassword(@RequestHeader Map<String, String> headers,
                                       @RequestParam("worknum") String worknum,
                                       @RequestParam("password") String password) {
        String token=headers.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");

        int permissions = (Integer)claims.get("permission");
        if(permissions<1){
            return JsonData.buildError("你暂无权限");
        }
        int k=collegeAdminService.updateUserPassword(worknum,password,department);
        if(k>0){
            redisUtils.del("login:"+worknum);//更换密码后需要重新登陆，删除缓存中的数据
        }
        return k>0?JsonData.buildSuccess():JsonData.buildError("修改失败");
    }

    /**
     * 修改成员手机号码
     * @param headers
     * @param worknum
     * @param phone
     * @return
     */
    @PostMapping("/updateUserPhone")
    public JsonData updateUserPhone(@RequestHeader Map<String, String> headers,
                                    @RequestParam("worknum") String worknum,
                                    @RequestParam("phone") String phone){
        String token=headers.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");

        int permissions = (Integer)claims.get("permission");
        if(permissions<1){
            return JsonData.buildError("你暂无权限");
        }
        int k=collegeAdminService.uodateUserPhone(worknum,phone,department);
        return k>0?JsonData.buildSuccess():JsonData.buildError("修改失败");
    }

    /**
     * 根据部门条件选择查询建设类
     * @param header
     * @param worknum
     * @param class2
     * @param class3
     * @param schoolyear
     * @param year
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getDptConstructions")
    public JsonData getDptConstructions(@RequestHeader Map<String ,String> header,
                                        @RequestParam("worknum") String worknum ,
                                        @RequestParam("class2") String class2,
                                        @RequestParam("class3") String class3,
                                        @RequestParam("level") String level,
                                        @RequestParam("schoolyear") String schoolyear,
                                        @RequestParam("year") String year,
                                        @RequestParam(value = "page",defaultValue = "1") int page,
                                        @RequestParam(value = "size",defaultValue = "30")int size){

        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");

        ConstructionInfo info=new ConstructionInfo();
        info.setClass2(class2);
        info.setClass3(class3);
        info.setSchoolyear(schoolyear);
        info.setYear(year);
        info.setWorknum(worknum);
        info.setDepartment(department);
        info.setLevel(level);
        PageHelper.startPage(page,size);
        List list=collegeAdminService.getDptConstructions(info);
        PageInfo pageInfo=new PageInfo<>(list);
        return JsonData.buildSuccess(pageInfo);

    }

    /**
     *根据部门条件选择查询成果类
     * @param header
     * @param worknum
     * @param class2
     * @param class3
     * @param schoolyear
     * @param year
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getDptAchievements")
    public JsonData getDptAchievements( @RequestHeader Map<String, String> header,
                                        @RequestParam("worknum") String worknum ,
                                        @RequestParam("class2") String class2,
                                        @RequestParam("class3") String class3,
                                        @RequestParam("schoolyear") String schoolyear,
                                        @RequestParam("year") String year,
                                        @RequestParam(value = "page",defaultValue = "1") int page,
                                        @RequestParam(value = "size",defaultValue = "30")int size) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        AchievementInfo achievementInfo=new AchievementInfo();
        achievementInfo.setSchoolYear(schoolyear);
        achievementInfo.setClass2(class2);
        achievementInfo.setClass3(class3);
        achievementInfo.setWorknum(worknum);
        achievementInfo.setYear(year);
        achievementInfo.setDepartment(department);
        PageHelper.startPage(page,size);
        List list=collegeAdminService.getDptAchievements(achievementInfo);
        PageInfo pageInfo=new PageInfo<>(list);
        return JsonData.buildSuccess(pageInfo);
    }

    /**
     * 根据部门条件选择查询获奖类
     * @param header
     * @param worknum
     * @param level
     * @param class3
     * @param prize
     * @param schoolyear
     * @param year
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getAwards")
    public JsonData getAwards(@RequestHeader Map<String, String> header,
                              @RequestParam("worknum") String worknum ,
                              @RequestParam("level") String level,
                              @RequestParam("class3") String class3,
                              @RequestParam("prize") String prize,
                              @RequestParam("schoolyear") String schoolyear,
                              @RequestParam("year") String year,
                              @RequestParam(value = "page",defaultValue = "1") int page,
                              @RequestParam(value = "size",defaultValue = "30")int size
                              ) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        AwardInfo  aw = new AwardInfo();
        aw.setClass3(class3);
        aw.setWorknum(worknum);
        aw.setLevel(level);
        aw.setSchoolYear(schoolyear);
        aw.setYear(year);
        aw.setPrize(prize);
        aw.setDepartment(department);
        PageHelper.startPage(page,size);
        List list=collegeAdminService.getDptAwards(aw);
        PageInfo pageInfo=new PageInfo<>(list);
        return JsonData.buildSuccess(pageInfo);
    }



}
