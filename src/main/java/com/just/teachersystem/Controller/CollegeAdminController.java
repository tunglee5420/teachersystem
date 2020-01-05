package com.just.teachersystem.Controller;


import com.github.andyczy.java.excel.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.just.teachersystem.Annotation.Logs;
import com.just.teachersystem.Service.CollegeAdminService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.Utill.RedisUtils;
import com.just.teachersystem.VO.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
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
     * 学院管理员获取确认表权限
     * @return
     */
    @Logs(role="collegeAdmin",description = "学院管理员获取确认表权限")
    @PostMapping("/getAdminEnterPermission")
    public JsonData getAdminPermission() {
        Map<Object,Object> map=redisUtils.hmget("Entrance:admin");
        if (map==null ||map.isEmpty()){
            return JsonData.buildError("获取出错");
        }
        return JsonData.buildSuccess(map);
    }


    /**
     * 获取部门用户
     * @param header
     * @param map//json 参数
     * @param page  页号
     * @param size 页大小
     * @return JsonData
     */
    @Logs(role="collegeAdmin",description = "获取部门用户")
    @PostMapping("/getDptUserInfo")
    public JsonData getDptUserInfo(@RequestHeader Map<String ,String> header,
                                   @RequestBody Map<String,String> map,
                                   @RequestParam(value = "page",defaultValue = "1") int page,
                                   @RequestParam(value = "size",defaultValue = "30")int size){
        PageHelper.startPage(page,size);
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        String worknum=map.get("department");
        String name=map.get("name");
        UserInfo userInfo = new UserInfo();
        userInfo.setDptname(department);
        if (worknum!=null) userInfo.setWorknum(worknum);
        if(name != null) userInfo.setName(name);
        List list=collegeAdminService.getUserInfo(userInfo);
        PageInfo<UserInfo> pageInfo = new PageInfo<> (list);
        return JsonData.buildSuccess(pageInfo);
    }


    /**
     * 修改部门成员密码
     * @param headers
     *  worknum 工号
     *  password 密码
     * @return
     */
    @Logs(role="collegeAdmin",description = "修改部门成员信息")
    @PostMapping("/updateDptUserInfo")
    public JsonData updateDptUserInfo(@RequestHeader Map<String, String> headers,
                                       @RequestBody UserInfo userInfo) {
        String token=headers.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        int permissions = (Integer)claims.get("permission");
        if(permissions<1){
            return JsonData.buildError("你暂无权限");
        }
        if(userInfo.getDptname().equals(department)){
            boolean res=collegeAdminService.updateUserInfo(userInfo);
            return res?JsonData.buildSuccess():JsonData.buildError("修改失败");
        }
        return JsonData.buildError("你暂无权限修改其他部门信息");
    }


    /**
     *暂时不需要
     * 根据部门条件选择查询建设类
     * @param header
     * @param worknum
     * @param class2
     * @param class3
     * @param schoolYear
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
                                        @RequestParam("schoolYear") String schoolYear,
                                        @RequestParam("year") String year,
                                        @RequestParam(value = "page",defaultValue = "1") int page,
                                        @RequestParam(value = "size",defaultValue = "30")int size){
        PageHelper.startPage(page,size);
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        ConstructionInfo info=new ConstructionInfo();
        info.setClass2(class2);
        info.setClass3(class3);
        info.setSchoolYear(schoolYear);
        info.setYear(year);
        info.setWorknum(worknum);
        info.setDepartment(department);
        info.setLevel(level);

        List list=collegeAdminService.getDptConstructions(info);
        PageInfo pageInfo=new PageInfo<>(list);
        return JsonData.buildSuccess(pageInfo);
    }

    /**
     *暂时不需要
     *根据部门条件选择查询成果类
     * @param header
     * @param worknum
     * @param class2
     * @param class3
     * @param schoolYear
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
                                        @RequestParam("schoolYear") String schoolYear,
                                        @RequestParam("year") String year,
                                        @RequestParam(value = "page",defaultValue = "1") int page,
                                        @RequestParam(value = "size",defaultValue = "30")int size) {
        PageHelper.startPage(page,size);
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        AchievementInfo achievementInfo=new AchievementInfo();
        achievementInfo.setSchoolYear(schoolYear);
        achievementInfo.setClass2(class2);
        achievementInfo.setClass3(class3);
        achievementInfo.setWorknum(worknum);
        achievementInfo.setYear(year);
        achievementInfo.setDepartment(department);

        List list=collegeAdminService.getDptAchievements(achievementInfo);
        PageInfo pageInfo=new PageInfo<>(list);
        return JsonData.buildSuccess(pageInfo);
    }

    /**
     * 暂时不需要
     * 根据部门条件选择查询获奖类
     * @param header
     * @param worknum
     * @param level
     * @param class3
     * @param prize
     * @param schoolYear
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
                              @RequestParam("schoolYear") String schoolYear,
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
        aw.setSchoolYear(schoolYear);
        aw.setYear(year);
        aw.setPrize(prize);
        aw.setDepartment(department);

        PageHelper.startPage(page,size);
        List list=collegeAdminService.getDptAwards(aw);
        PageInfo pageInfo=new PageInfo<>(list);
        return JsonData.buildSuccess(pageInfo);
    }

    /**
     * 暂时不需要
     * 获取确认业绩名单
     * @param header
     * @param year
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getConfirmPerformance")
    public JsonData getConfirmPerformance(@RequestHeader Map<String, String> header,
                                          @RequestParam("year") String year,
                                          @RequestParam(value = "page",defaultValue = "1") int page,
                                          @RequestParam(value = "size",defaultValue = "30")int size) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        PerformanceInfo performanceInfo=new PerformanceInfo();
        performanceInfo.setYear(year);
        performanceInfo.setDepartment(department);
        performanceInfo.setStatus(1);
        List<PerformanceInfo>list=collegeAdminService.confirmPerformance(performanceInfo);
        if (list==null)
            return JsonData.buildError("服务器出错");
        return JsonData.buildSuccess(list);
    }

    /**
     * 导出业绩excel确认表
     * @param header
     * @param year
     * @param response
     */

    @Logs(role="collegeAdmin",description = "导出业绩excel确认表")
    @PostMapping("/getConfirmPerformanceExcel")
    public void getConfirmPerformanceExcel(@RequestHeader Map<String, String> header,
                                               @RequestParam("year") String year,
                                               HttpServletResponse response) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        PerformanceInfo performanceInfo=new PerformanceInfo();
        performanceInfo.setYear(year);

        performanceInfo.setDepartment(department);
        performanceInfo.setStatus(1);
        List<PerformanceInfo>list=collegeAdminService.confirmPerformance(performanceInfo);

        //设置表格
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头
        String[]labels ={year+"校区、苏理工（"+department+"）教学建设各项业绩分补贴复核表"};
        excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"部门	","所属科室","类别","立项年度","名称","负责人","业绩分","签字确认"};
        String []confirmed = new String[] {"部门领导："," "," ","经办人： "," "," 日期："," ","  "};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);
        data.add(a);
        String []values=null;
        int sum=0;
        for (PerformanceInfo p:list) {
            sum+=p.getPoints();
            values= new String[]{p.getDepartment(), p.getComputeoffice(),p.getType(),p.getYear(),p.getProject(),p.getMaster(),String.valueOf(p.getPoints())," "};

            a.add(values);
        }
        String[]all=new String [] {department+"汇总","","","","","",String.valueOf(sum),""};

        a.add(all);
        a.add(confirmed);
        //合并单元格格式（初始行，末尾行，初始列，末尾列）
        HashMap<Integer,Object>regionMap=new HashMap<> () ;
        ArrayList<Integer[]>arrayList = new ArrayList <> ();
        arrayList.add(new Integer[]{list.size()+2,list.size()+2,0,2});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,0,2});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,3,4});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,5,7});
        regionMap.put(1,arrayList);
        excelUtils.setRegionMap(regionMap);
        excelUtils.setDataLists(data);

        //设置excel 名称
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(year+"校区、苏理工（"+department+"）教学建设各项业绩分补贴复核表");

        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();
    }


    /**
     * 暂时不需要
     * 获取确认津贴名单
     * @param header
     * @param year
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getConfirmBonus")
    public JsonData getConfirmBonus(@RequestHeader Map<String, String> header,
                                          @RequestParam("year") String year,
                                          @RequestParam(value = "page",defaultValue = "1") int page,
                                          @RequestParam(value = "size",defaultValue = "30")int size) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        BonusInfo bonus = new BonusInfo();
        bonus.setYear(year);
        bonus.setDepartment(department);
        bonus.setStatus(1);
        List <BonusInfo> list=collegeAdminService.confirmBonus(bonus);

        if (list==null)
            return JsonData.buildError("服务器出错");
        return JsonData.buildSuccess(list);

    }

    /**
     * 导出奖金excel确认表
     * @param header
     * @param year
     * @param response
     */
    @Logs(role="collegeAdmin",description = "导出奖金excel确认表")
    @PostMapping("/getConfirmBonusExcel")
    public void getConfirmBonusExcel(@RequestHeader Map<String, String> header,
                                           @RequestParam("year") String year,
                                           HttpServletResponse response) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=( String) claims.get("department");
        BonusInfo bonus = new BonusInfo();
        bonus.setYear(year);
        bonus.setDepartment(department);
        bonus.setStatus(1);
        List <BonusInfo> list=collegeAdminService.confirmBonus(bonus);


        //设置表格
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头
        String[]labels ={year+"校区、苏理工专业负责人津贴、高水平教学成果及改革建设立项奖励明细复核表("+department+")"};
        excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"部门	","所属科室","类别","立项年度","名称","负责人","汇总(元)","签字确认"};
        String []confirmed = new String[] {"部门领导："," "," ","经办人： "," "," 日期："," ","  "};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);
        data.add(a);
        String []values=null;
        double sum=0;

        for (BonusInfo b:list) {
            sum+=b.getBonus();
            values= new String[]{b.getDepartment(), b.getComputeoffice(),b.getType(),b.getYear(),b.getProject(),b.getMaster(),String.valueOf(b.getClass())," "};
            a.add(values);
        }
        String[]all=new String [] {department+"汇总","","","","","",String.valueOf(sum),""};

        a.add(all);
        a.add(confirmed);
        //合并单元格格式（初始行，末尾行，初始列，末尾列）
        HashMap<Integer,Object>regionMap=new HashMap<> () ;
        ArrayList<Integer[]>arrayList = new ArrayList <> ();
        arrayList.add(new Integer[]{list.size()+2,list.size()+2,0,2});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,0,2});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,3,4});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,5,7});
        regionMap.put(1,arrayList);
        excelUtils.setRegionMap(regionMap);
        excelUtils.setDataLists(data);

        //设置excel 名称
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(year+"校区、苏理工（"+department+"）教学建设各项业绩分补贴复核表");

        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();
    }
}
