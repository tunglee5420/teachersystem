package com.just.teachersystem.Controller;

import java.util.ArrayList;
import	java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.github.andyczy.java.excel.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.teachersystem.Entity.Kind;

import com.just.teachersystem.Service.RootService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.RedisUtils;
import com.just.teachersystem.VO.BonusInfo;
import com.just.teachersystem.VO.PerformanceInfo;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/online/root")
public class RootController {
    @Autowired
    RootService root;
    @Autowired
    RedisUtils redisUtils;


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
     * 添加用户信息
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
     * 设置用户权限
     * @return
     */
    @PostMapping("/setPermission")
    public JsonData setPermission(@RequestParam("permission") int permission){
        if(permission<0||permission>3)
            return JsonData.buildError("设置出错");
        UserInfo userInfo=new UserInfo();
        userInfo.setPermission(permission);
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
     * 根据条件筛选信息
     * @param department
     * @param worknum
     * @param name
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getUserList")
    public JsonData getUserList(
            @RequestParam(value = "department",defaultValue ="") String department,
            @RequestParam(value = "worknum",defaultValue ="") String worknum ,
            @RequestParam(value = "name",defaultValue ="") String name,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "30")int size){
        UserInfo userInfo = new UserInfo();
        PageHelper.startPage(page,size);
        if(!(department==null || department.equals("")))  userInfo.setDptname(department);
        if(!(worknum==null || worknum.equals("")))userInfo.setWorknum(worknum);
        if(!(name==null || name.equals(""))) userInfo.setName(name);

        List list=root.getUserInfo(userInfo);
        if(userInfo==null) return JsonData.buildError("服务器出错");
        PageInfo<UserInfo> pageInfo = new PageInfo<UserInfo> (list);
        return JsonData.buildSuccess(pageInfo);
    }

    /**
     * 下载用户表
     */
    @PostMapping("/getUserExcel")
    public void getUserExcel(HttpServletResponse response) {
        UserInfo userInfo = new UserInfo();
        List<UserInfo>list=root.getUserInfo(userInfo);



        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工教职工人信息表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"部门","姓名","工号","性别","出生年月",
                "入校时间","电话","专业技术职称","最高学历","最高学位","授学位单位名称","获最高学位的专业名称","是否双师型",
                "是否具有行业背景","是否博硕士生导师"};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);

        String []values=null;

        for (UserInfo p:list) {

            values= new String[]{p.getDptname(), p.getName(),p.getWorknum(),p.getGender(), String.valueOf(p.getBirthday()),
                    String.valueOf(p.getEnterTime()),p.getPhone(),p.getTechTittle(),p.getEduBgd(),p.getDegree(),p.getSchool(),
                    p.getMajor(),p.getDoubleTeacher()==0?"不是":"是", p.getBackground()==0?"不是":"是", p.getTutor()==0?"不是":"是"};

            a.add(values);
        }

        data.add(a);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();
    }


    /**
     * 条件筛选选业绩分信息
     * @return
     */
    @PostMapping("/getPerfromanceInfo")
    public JsonData getPerfromanceInfo(
            @RequestParam(value = "department",defaultValue ="") String department,
            @RequestParam(value = "year",defaultValue ="") String year ,
            @RequestParam(value = "master",defaultValue ="") String master,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10")int size){

        PerformanceInfo performanceInfo=new PerformanceInfo();
        performanceInfo.setDepartment(department);
        performanceInfo.setYear(year);
        performanceInfo.setMaster(master);
        List list=root.getPerfromanceList(performanceInfo);

        PageHelper.startPage(page,size);
        if(list==null) return JsonData.buildError("服务器错误");
        PageInfo pageInfo=new PageInfo(list);
        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 条件导出业绩信息
     * @param response
     * @param department
     * @param year
     * @param master
     */
    @PostMapping("/getPerformanceExcel")
    public void getPerformanceExcel(HttpServletResponse response,
                                    @RequestParam(value = "department",defaultValue ="") String department,
                                    @RequestParam(value = "year",defaultValue ="") String year ,
                                    @RequestParam(value = "master",defaultValue ="") String master){
        PerformanceInfo performanceInfo=new PerformanceInfo();
        performanceInfo.setDepartment(department);
        performanceInfo.setYear(year);
        performanceInfo.setMaster(master);
        List<PerformanceInfo> list=root.getPerfromanceList(performanceInfo);

        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={year+"校区、苏理工教师业绩信息表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"院部","业绩分计算科室","分类—类别","立项年度","项目名称", "负责人","业绩分（分）"};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);

        String []values=null;

        for (PerformanceInfo p:list) {
            values= new String[]{p.getDepartment(), p.getComputeoffice(),p.getType(),p.getYear(), p.getProject(), p.getMaster(), String.valueOf(p.getPoints())};
            a.add(values);
        }
        data.add(a);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }


    /**
     * 根据id 删除业绩分信息
     * @param id

     * @return
     */
    @DeleteMapping("/deletePerformance")
    public JsonData deletePerformance(@RequestParam("id") int id){
        PerformanceInfo p=new PerformanceInfo();
        p.setId(id);
        p.setStatus(0);
        if(root.updatePerformanceInfo(p))
            return JsonData.buildSuccess("删除成功");
        return  JsonData.buildError("删除失败");
    }

    /**
     * 修改业绩信息
     * @param performanceInfo
     * @return
     */
    @PostMapping("/deletePerformance")
    public JsonData updatePerformance(PerformanceInfo performanceInfo){
        if(performanceInfo==null||performanceInfo.getStatus()==0) return JsonData.buildError("传过来的值为空");
        if(root.updatePerformanceInfo(performanceInfo))
            return JsonData.buildSuccess("修改成功");
        return  JsonData.buildError("修改失败");
    }

    /**
     * 添加业绩信息
     * @param performance
     * @return
     */
    @PostMapping("/addPerformance")
    public JsonData addPerformance(PerformanceInfo performance) {
        if(performance==null) return JsonData.buildError("传过来的值为空");
        if(root.addPerformanceInfo(performance))
            return JsonData.buildSuccess("添加成功");
        return  JsonData.buildError("添加失败");
    }


    /**
     * 条件筛选奖金信息
     * @return
     */
    @PostMapping("/getBonusInfo")
    public JsonData getBonusInfo(
            @RequestParam(value = "department",defaultValue ="") String department,
            @RequestParam(value = "year",defaultValue ="") String year ,
            @RequestParam(value = "master",defaultValue ="") String master,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10")int size){

        BonusInfo bonusInfo=new BonusInfo();
        bonusInfo.setDepartment(department);
        bonusInfo.setYear(year);
        bonusInfo.setMaster(master);
        List list=root.getBonusList(bonusInfo);

        PageHelper.startPage(page,size);
        if(list==null) return JsonData.buildError("服务器错误");
        PageInfo pageInfo=new PageInfo(list);
        return JsonData.buildSuccess(pageInfo);

    }


    /**
     * 条件导出奖金信息
     * @param response
     * @param department
     * @param year
     * @param master
     */
    @PostMapping("/getBonusExcel")
    public void getBonusExcel(HttpServletResponse response,
                                    @RequestParam(value = "department",defaultValue ="") String department,
                                    @RequestParam(value = "year",defaultValue ="") String year ,
                                    @RequestParam(value = "master",defaultValue ="") String master){
        BonusInfo bonusInfo=new BonusInfo();
        bonusInfo.setDepartment(department);
        bonusInfo.setYear(year);
        bonusInfo.setMaster(master);
        List<BonusInfo> list=root.getBonusList(bonusInfo);

        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={year+"校区、苏理工教师奖金信息表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"院部","奖金计算科室（内置）","分类—类别","立项年度","项目名称", "负责人","奖金（元）"};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);

        String []values=null;

        for (BonusInfo b:list) {
            values= new String[]{b.getDepartment(), b.getComputeoffice(),b.getType(),b.getYear(), b.getProject(), b.getMaster(), String.valueOf(b.getBonus())};
            a.add(values);
        }
        data.add(a);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 根据id 删除奖金信息
     * @param id

     * @return
     */
    @DeleteMapping("/deleteBonus")
    public JsonData deleteBonus(@RequestParam("id") int id){
        BonusInfo b=new BonusInfo();
        b.setId(id);
        b.setStatus(0);
        if(root.updateBonusInfo(b))
            return JsonData.buildSuccess("删除成功");
        return  JsonData.buildError("删除失败");
    }

    /**
     * 修改奖金信息
     * @param bonusInfo
     * @return
     */
    @PostMapping("/deleteBonus")
    public JsonData deleteBonus(BonusInfo bonusInfo){
        if(bonusInfo==null||bonusInfo.getStatus()==0) return JsonData.buildError("传过来的值为空");
        if(root.updateBonusInfo(bonusInfo))
            return JsonData.buildSuccess("修改成功");
        return  JsonData.buildError("修改失败");
    }

    /**
     * 添加奖金信息
     * @param bonusInfo
     * @return
     */
    @PostMapping("/addBonus")
    public JsonData addBonus(BonusInfo bonusInfo) {
        if(bonusInfo==null) return JsonData.buildError("传过来的值为空");
        if(root.addBonusInfo(bonusInfo))
            return JsonData.buildSuccess("添加成功");
        return  JsonData.buildError("添加失败");
    }


}
