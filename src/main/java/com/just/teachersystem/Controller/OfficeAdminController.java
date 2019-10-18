package com.just.teachersystem.Controller;
import java.time.Year;
import java.util.*;

import com.github.andyczy.java.excel.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.teachersystem.Exception.MyException;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Service.OfficeAdminService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.PerformanceInfo;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/online/officeAdmin")
public class OfficeAdminController {
    @Autowired
    OfficeAdminService officeAdminService;
    @Autowired
    CommonService commonService;

    /**
     * 获取用户提交的建设类信息
     * @param header
     * @param construction
     * @return
     */
    @PostMapping("/getUserConstruction")
    public JsonData getUserConstruction(@RequestHeader Map<String ,String> header, ConstructionInfo construction,
                                        @RequestParam(value = "page",defaultValue = "1") int page,
                                        @RequestParam(value = "size",defaultValue = "30")int size
                                        ){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            return JsonData.buildError("你没有权限");
        }
        List<ConstructionInfo> list=officeAdminService.getUserConstruction(construction);
        PageHelper.startPage(page,size);
        PageInfo<ConstructionInfo> pageInfo=new PageInfo<> (list);

        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 导出建设类excel
     * @param header
     * @param year
     * @param response
     */

    @PostMapping("/getConstructionExcel")
    public void getConstructionExcel(@RequestHeader Map<String, String> header,
                                     @RequestParam("year") String year,
                                     @RequestParam("schoolYear")String schoolYear,
                                     HttpServletResponse response) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw  new MyException("你没有权限");
        }
        ConstructionInfo construction=new ConstructionInfo();
        construction.setYear(year);
        construction.setSchoolyear(schoolYear);
        List<ConstructionInfo> list=officeAdminService.getUserConstruction(construction);

        //设置表格
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={year+"校区、苏理工教学建设各项业绩分表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"序号","项目编号","院部","项目名称","负责人","工号","课题组成员",
                "立项时间","项目起止年月","主办单位","分类","类别","级别","建设经费(元)",
                "业绩分","业绩分计算年度","奖金(元)","归档文件盒编号","是否结束","学年","年度"};
        //  String []confirmed = new String[] {"部门领导："," "," ","经办人： "," "," 日期："," ","  "};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);
        data.add(a);
        String []values=null;
        for (ConstructionInfo p:list) {

            values= new String[]{String.valueOf(p.getCid()), p.getProjectNum(),p.getDepartment(),p.getProject(),p.getName(),p.getWorknum(),
                    p.getTeammate(),p.getStartTime(),p.getBeginToEndTime(),p.getSponsor(),p.getClass2(),p.getClass3(),
                    p.getLevel(),String.valueOf(p.getExpenditure()),String.valueOf(p.getPoint()),p.getComputeYear(),String.valueOf(p.getBonus()),p.getFileNumber(),
                    p.getIsEnd()==0?"否":"是",p.getSchoolyear(),p.getYear()};

            a.add(values);
        }

        //设置excel 名称
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();
    }
    /**
     * 补充建设信息并核审
     * @param header
     * @param construction
     * @return
     */
    @PostMapping("/ConstructionSupplement")
    public JsonData ConstructionSupplement(@RequestHeader Map<String ,String> header, ConstructionInfo construction){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            return JsonData.buildError("你没有权限");
        }
        boolean res=commonService.updateConstructionServ(construction);
        if(res){
            return JsonData.buildSuccess("操作成功");

        }
        return JsonData.buildError("操作失败");
    }

    /**
     * 获取用户提交的成果类信息
     * @param header
     * @param achievement
     * @return
     */
    @PostMapping("/getUserAchievement")
    public JsonData getUserAchievement(@RequestHeader Map<String ,String> header, AchievementInfo achievement,
                                       @RequestParam(value = "page",defaultValue = "1") int page,
                                       @RequestParam(value = "size",defaultValue = "30")int size) {
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String department = (String) claims.get("department");
        if (!department.equals("质量建设与评估办公室")) {
            return JsonData.buildError("你没有权限");
        }
        List<AchievementInfo> list=officeAdminService.getUserAchievement(achievement);
        PageHelper.startPage(page,size);
        PageInfo<AchievementInfo> pageInfo=new PageInfo<> (list);

        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 导出成果类excel
     * @param header
     * @param year
     * @param response
     */
    @PostMapping("/getAchievementExcel")
    public void getAchievementExcel(@RequestHeader Map<String, String> header,
                                    @RequestParam("year") String year,
                                    @RequestParam("schoolYear")String schoolYear,
                                    HttpServletResponse response) {
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String department = (String) claims.get("department");
        if (!department.equals("质量建设与评估办公室")) {
            throw  new MyException("你没有权限");
        }
        AchievementInfo achievement=new AchievementInfo();
        achievement.setYear(year);
        achievement.setSchoolYear(schoolYear);
        List<AchievementInfo> list=officeAdminService.getUserAchievement(achievement);

//设置表格
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={year+"校区、苏理工成果类表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"序号","院部名称","第一作者","工号","组员","成果名称","成果类别","级别",
                "发表刊物/出版社/授权单位","发表时间/出版时间/授权时间（年/月）","是否被转让或采用（仅限于专利）",
                "证书","学年","年度"};
        //  String []confirmed = new String[] {"部门领导："," "," ","经办人： "," "," 日期："," ","  "};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);
        data.add(a);
        String []values=null;
        for (AchievementInfo p:list) {

            values= new String[]{String.valueOf(p.getAid()),p.getDepartment(),p.getName(),p.getWorknum(),p.getTeammate(),p.getProduction(),
                    p.getClass2(),p.getClass3(),p.getUnit(),String.valueOf(p.getPublishTime()),p.getPatent()==0?"否":"是",p.getCertificate(),
                    p.getSchoolYear(),p.getYear()};

            a.add(values);
        }

        //设置excel 名称
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);

        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 补充成果信息并核审
     * @param header
     * @param info
     * @return
     */
    @PostMapping("/AchievementSupplement")
    public JsonData AchievementSupplement(@RequestHeader Map<String, String> header, AchievementInfo info) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            return JsonData.buildError("你没有权限");
        }
        boolean res=commonService.updateAchievementServ(info);
        if(res){
            return JsonData.buildSuccess("操作成功");
        }
        return JsonData.buildError("操作失败");
    }



    /**
     * 获取用户提交的获奖类信息
     * @param header
     * @param awardInfo
     * @return
     */
    @PostMapping("/getUserAchievement")
    public JsonData getUserAward(@RequestHeader Map<String ,String> header, AwardInfo awardInfo,
                                       @RequestParam(value = "page",defaultValue = "1") int page,
                                       @RequestParam(value = "size",defaultValue = "30")int size) {
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String department = (String) claims.get("department");
        if (!department.equals("质量建设与评估办公室")) {
            return JsonData.buildError("你没有权限");
        }
        List<AwardInfo> list=officeAdminService.getUserAward(awardInfo);
        PageHelper.startPage(page,size);
        PageInfo<AwardInfo> pageInfo=new PageInfo<> (list);

        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 补充获奖类信息并核审
     * @param header
     * @param awardInfo
     * @return
     */
    @PostMapping("/AwardSupplement")
    public JsonData AwardSupplement(@RequestHeader Map<String, String> header, AwardInfo awardInfo) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            return JsonData.buildError("你没有权限");
        }
        boolean res=commonService.updateAwardServ(awardInfo);
        if(res){
            return JsonData.buildSuccess("操作成功");
        }
        return JsonData.buildError("操作失败");
    }

    @PostMapping("/getAwardExcel")
    public void getAwardExcel(@RequestHeader Map<String ,String> header,
                              @RequestParam("year") String year,
                              @RequestParam("schoolYear")String schoolYear,
                              HttpServletResponse response){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw new MyException("你暂无权限");
        }
        AwardInfo awardInfo=new AwardInfo();
        awardInfo.setYear(year);
        awardInfo.setSchoolYear(schoolYear);
        List<AwardInfo>list=officeAdminService.getUserAward(awardInfo);
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={year+"校区、苏理工教学获奖表"};
        //excelUtils.setLabelName(labels);
        //设置字段
        String []params=new String [] {"序号","	院部名称","	获奖教师（排名第1）","工号","获奖成员","获奖内容",
                "分类","类别","级别","奖项","颁奖部门","奖金计算年度","奖金（元）","证书",
                "获奖时间","学年","年度"};
        //  String []confirmed = new String[] {"部门领导："," "," ","经办人： "," "," 日期："," ","  "};
        excelUtils.setLabelName(labels);
        List<String[]> a=new ArrayList<> ();
        a.add(params);
        data.add(a);
        String []values=null;

        for (AwardInfo p:list) {

            values= new String[]{String.valueOf(p.getAid()), p.getDepartment(),p.getName(),p.getWorknum(),p.getTeammate(),p.getContent(),
                    p.getClass2(),p.getClass3(),p.getLevel(),p.getPrize(),p.getAwardUnit(),p.getAwardYear(),
                    String.valueOf(p.getBonus()),p.getCertificate(), String.valueOf(p.getAwardTime()),p.getSchoolYear(),p.getYear()};

            a.add(values);
        }

        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);

        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();

    }


    @PostMapping("/excelImportPerformance")
    public JsonData excelImportPerformance(@RequestParam("file") MultipartFile file,@RequestParam("sheetIndex") int index){
        ExcelUtils excelUtils=ExcelUtils.initialization();
        Workbook workbook= (Workbook) file;
        int num=workbook.getNumberOfSheets();
        if(index>=num||index<0) return JsonData.buildError("你选择的表有误");
        String[] sheets = new String [num];
        for (int i=0;i<num;i++) {
            sheets [i]= workbook.getSheetName(i);
        }
        excelUtils.setExpectDateFormatStr("yyyy-MM-dd");
        HashMap<Integer,Integer> hashMap = new HashMap<> ();
        hashMap.put(index,2);//从第一张表第二行
        List<List<LinkedHashMap<String, String>>> sheetList= ExcelUtils.importForExcelData(workbook,sheets,hashMap,null);

        List<LinkedHashMap<String, String>> rowList =sheetList.get(index);
        List<PerformanceInfo> performanceInfoList=new ArrayList<>();
        PerformanceInfo p=null;
        for (int i=0;i<rowList.size(); i++) {
            p=new PerformanceInfo();
            p.setDepartment(rowList.get(i).get(0));
            p.setComputeoffice(rowList.get(i).get(1));
            p.setType(rowList.get(i).get(2));
            p.setYear(rowList.get(i).get(3));
            p.setProject(rowList.get(i).get(4));
            p.setMaster(rowList.get(i).get(5));
            p.setPoints(Integer.parseInt(rowList.get(i).get(6)));
            performanceInfoList.add(p);
        }
        boolean res=officeAdminService.insertToPerformanceList(performanceInfoList);
        if(res){
            return JsonData.buildSuccess("导入成功");
        }
        return JsonData.buildError("导入失败");


    }









}
