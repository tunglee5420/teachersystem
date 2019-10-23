package com.just.teachersystem.Controller;
import	java.util.HashMap;
import	java.sql.Ref;
import java.util.Map;

import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.Entity.User;
import com.just.teachersystem.Service.RootService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.RedisUtils;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/online/root")
public class RootController {
    @Autowired
    RootService root;
    @Autowired
    RedisUtils redisUtils;

    /**
     * 添加分类
     * @param kind
     * @return
     */
    @PostMapping("/addType")
    public JsonData addType(Kind kind){
//        System.out.println(kind.toString());
        boolean is = root.addType(kind);
        if(is){
            return JsonData.buildSuccess("添加成功");
        }
        return JsonData.buildError("添加失败");

    }

    /**
     * 删除类别
     * @param class3
     * @return
     */
    @DeleteMapping("/deleteType")
    public JsonData deleteType(@RequestParam("class3") String class3){
        boolean is = root.deleteType(class3);
        if(is){
            return JsonData.buildSuccess("删除成功");
        }
        return JsonData.buildError("删除失败");
    }
    /**
     * 添加级别
     * @param level
     * @return
     */
    @PostMapping("/addLevel")
    public JsonData addType(@RequestParam("level") String level) {
        boolean is = root.addLevel(level);
        if(is){
            return JsonData.buildSuccess("添加成功");
        }
        return JsonData.buildError("添加失败");
    }

    /**
     * 删除级别
     * @param level
     * @return
     */
    @DeleteMapping("/deleteLevel")
    public JsonData deleteLevel(@RequestParam("level") String level){
        boolean is=root.deleteLevel(level);
        if(is){
            return JsonData.buildSuccess("删除成功");

        }
        return JsonData.buildError("删除失败");
    }

    /**
     * 插入用户信息
     * @param userInfo
     * @return
     */
    @PostMapping("/addUserInfo")
    public JsonData addUserInfo(UserInfo userInfo) {
        if (userInfo==null||userInfo.getWorknum()==null) return JsonData.buildError("信息为空");
        boolean res=root.addUser(userInfo);
        if (res)
            return JsonData.buildSuccess("添加成功");
        return JsonData.buildError("添加失败");
    }




    /**
     * 更新用户信息(包括修改密码和权限)
     * @param userInfo
     * @return
     */
    @PostMapping("/updateUserInfo")
    public JsonData updateUserInfo( UserInfo userInfo){
        if (userInfo==null) return JsonData.buildError("信息为空");
        boolean res=root.updateUserInfo(userInfo);
        return JsonData.buildSuccess(res);
    }

    /**
     * 根据工号删除用户
     * @param worknum
     * @return
     */
    @DeleteMapping("/deleteUser")
    public JsonData deleteUser(@RequestParam("worknum") String worknum){
        if(worknum==null || worknum.equals("")){
            return JsonData.buildError("工号为空");
        }
        boolean res = root.deleteUser(worknum);
        if(res)
            return JsonData.buildSuccess("删除成功");
        return JsonData.buildError("删除失败");
    }

    /**
     * 控制信息录入入口开关
     * @param construction
     * @param achievement
     * @param award
     * @return
     */
    @PostMapping("/manageUserEntrance")
    public JsonData manageUserEntrance(@RequestParam(value="constructionEntrance" ,defaultValue ="false" ) boolean construction,
                                   @RequestParam(value = "achievementEntrance" ,defaultValue ="false" ) boolean achievement,
                                   @RequestParam(value = "awardEntrance" ,defaultValue = "false") boolean award){
        Map<String,Object>map = new HashMap<> ();
        map.put("construction", construction);
        map.put("achievement ", achievement);
        map.put("award", award);
        boolean res=redisUtils.hmset("Entrance:user",map);
        if(res) return JsonData.buildSuccess("操作成功");
        return JsonData.buildError("操作失败");

    }

    /**
     * 控制管理员获取入口开关
     * @param performance
     * @param bonus
     * @return
     */
    @PostMapping("/manageAdminEntrance")
    public JsonData manageAdminEntrance(@RequestParam(value="performance" ,defaultValue ="false" ) boolean performance,
                                        @RequestParam(value="bonus" ,defaultValue ="false" ) boolean bonus) {
        Map<String,Object>map = new HashMap<> ();
        map.put("performance", performance);
        map.put("bonus", bonus);

        boolean res=redisUtils.hmset("Entrance:admin",map);
        if(res) return JsonData.buildSuccess("操作成功");
        return JsonData.buildError("操作失败");
    }

}
