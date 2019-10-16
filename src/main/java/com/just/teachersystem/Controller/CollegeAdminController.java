package com.just.teachersystem.Controller;
import	java.math.BigDecimal;

import com.github.andyczy.java.excel.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.teachersystem.Entity.Bonus;
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

    /**
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
     * 导出业绩excel
     * @param header
     * @param year
     * @param response
     */
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
     * 导出业绩excel
     * @param header
     * @param year
     * @param response
     */
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
        String []params=new String [] {"部门	","所属科室","类别","立项年度","名称","负责人","汇总（元）","签字确认"};
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
