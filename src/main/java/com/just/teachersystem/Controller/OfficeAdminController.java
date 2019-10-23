package com.just.teachersystem.Controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import com.github.andyczy.java.excel.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.teachersystem.Exception.MyException;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Service.OfficeAdminService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.VO.*;
import io.jsonwebtoken.Claims;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
     * @param department
     * @param class3
     * @param level
     * @param year
     * @param schoolYear
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getUserConstruction")
    public JsonData getUserConstruction(@RequestHeader Map<String ,String> header,
                                        @RequestParam(value = "department")String department,
                                        @RequestParam("class3")String class3,
                                        @RequestParam("level") String level,
                                        @RequestParam("year") String year,
                                        @RequestParam("schoolYear")String schoolYear,
                                        @RequestParam(value = "page",defaultValue = "1") int page,
                                        @RequestParam(value = "size",defaultValue = "30")int size
                                        ){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String dpt=(String) claims.get("department");
        if(!dpt.equals("质量建设与评估办公室")){
            return JsonData.buildError("你没有权限");
        }
        ConstructionInfo construction=new ConstructionInfo();
        construction.setSchoolyear(schoolYear);
        construction.setYear(year);
        construction.setLevel(level);
        construction.setClass3(class3);
        construction.setDepartment(department);

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
                                     @RequestParam(value = "schoolYear")String schoolYear,
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

        String []values=null;
        for (ConstructionInfo p:list) {

            values= new String[]{String.valueOf(p.getCid()), p.getProjectNum(),p.getDepartment(),p.getProject(),p.getName(),p.getWorknum(),
                    p.getTeammate(),p.getStartTime(),p.getBeginToEndTime(),p.getSponsor(),p.getClass2(),p.getClass3(),
                    p.getLevel(),String.valueOf(p.getExpenditure()),String.valueOf(p.getPoint()),p.getComputeYear(),String.valueOf(p.getBonus()),p.getFileNumber(),
                    p.getIsEnd()==0?"否":"是",p.getSchoolyear(),p.getYear()};

            a.add(values);
        }
        data.add(a);
        excelUtils.setDataLists(data);

        //设置excel 名称
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();
    }
    /**
     * 补充修改建设信息并核审
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
     * @param department
     * @param class3
     * @param year
     * @param schoolYear
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getUserAchievement")
    public JsonData getUserAchievement(@RequestHeader Map<String ,String> header,
                                       @RequestParam(value = "department")String department,
                                       @RequestParam("class3")String class3,
                                       @RequestParam("year") String year,
                                       @RequestParam("schoolYear")String schoolYear,
                                       @RequestParam(value = "page",defaultValue = "1") int page,
                                       @RequestParam(value = "size",defaultValue = "30")int size) {
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String dpt = (String) claims.get("department");
        if (!dpt.equals("质量建设与评估办公室")) {
            return JsonData.buildError("你没有权限");
        }
        AchievementInfo achievement=new AchievementInfo();
        achievement.setYear(year);
        achievement.setClass3(class3);
        achievement.setSchoolYear(schoolYear);
        achievement.setDepartment(department);
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

        String []values=null;
        for (AchievementInfo p:list) {

            values= new String[]{String.valueOf(p.getAid()),p.getDepartment(),p.getName(),p.getWorknum(),p.getTeammate(),p.getProduction(),
                    p.getClass2(),p.getClass3(),p.getUnit(),String.valueOf(p.getPublishTime()),p.getPatent()==0?"否":"是",p.getCertificate(),
                    p.getSchoolYear(),p.getYear()};

            a.add(values);
        }
        data.add(a);
        excelUtils.setDataLists(data);
        //设置excel 名称
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);

        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 补充修改成果信息并核审
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
     * @param department
     * @param class3
     * @param level
     * @param year
     * @param prize
     * @param schoolYear
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getUserAward")
    public JsonData getUserAward(@RequestHeader Map<String ,String> header,
                                 @RequestParam(value = "department")String department,
                                 @RequestParam("class3")String class3,
                                 @RequestParam("level") String level,
                                 @RequestParam("year") String year,
                                 @RequestParam("prize") String prize,
                                 @RequestParam("schoolYear")String schoolYear,
                                 @RequestParam(value = "page",defaultValue = "1") int page,
                                 @RequestParam(value = "size",defaultValue = "30") int size ){
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String dpt = (String) claims.get("department");
        if (!dpt.equals("质量建设与评估办公室")) {
            return JsonData.buildError("你没有权限");
        }
        AwardInfo awardInfo=new AwardInfo();
        awardInfo.setSchoolYear(schoolYear);
        awardInfo.setYear(year);
        awardInfo.setPrize(prize);
        awardInfo.setClass3(class3);
        awardInfo.setDepartment(department);
        awardInfo.setLevel(level);
        List<AwardInfo> list=officeAdminService.getUserAward(awardInfo);
        PageHelper.startPage(page,size);
        PageInfo<AwardInfo> pageInfo=new PageInfo<> (list);

        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 补充修改获奖类信息并核审
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

    /**
     * 获取获奖类excel
     * @param header
     * @param year
     * @param schoolYear
     * @param response
     */
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

        String []values=null;

        for (AwardInfo p:list) {

            values= new String[]{String.valueOf(p.getAid()), p.getDepartment(),p.getName(),p.getWorknum(),p.getTeammate(),p.getContent(),
                    p.getClass2(),p.getClass3(),p.getLevel(),p.getPrize(),p.getAwardUnit(),p.getAwardYear(),
                    String.valueOf(p.getBonus()),p.getCertificate(), String.valueOf(p.getAwardTime()),p.getSchoolYear(),p.getYear()};

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
     * 科室管理员按照模板来导入业绩分数据
     * @param file
     * @param index
     * @return
     */
    @PostMapping("/excelImportPerformance")
    public JsonData excelImportPerformance(@RequestParam("file") MultipartFile file,@RequestParam(value = "sheetIndex",defaultValue="1") int index){
        ExcelUtils excelUtils=ExcelUtils.initialization();
        Workbook wb = null;
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        InputStream ins = null;

        if(file.equals("")||file.getSize()<=0) return JsonData.buildError("文件为空");
        else {

            try {
                ins = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 根据后缀名称判断excel的版本
        }
        try {
            if (".xls".equals(extName)) {
                wb = new HSSFWorkbook(ins);
            } else if (".xlsx".equals(extName)) {
                wb = new XSSFWorkbook(ins);
            } else {
                // 无效后缀名称，这里之能保证excel的后缀名称，不能保证文件类型正确，不过没关系，在创建Workbook的时候会校验文件格式
                throw new IllegalArgumentException("Invalid excel version");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int num=wb.getNumberOfSheets();
//        System.out.println(num);
        if(index>num||index<0) return JsonData.buildError("你选择的表有误");
        String[] sheets = new String [num];
        for (int i=0;i<num; i++){
            sheets[i]=wb.getSheetName(i);
        }
        excelUtils.setExpectDateFormatStr("yyyy-MM-dd");
        HashMap<Integer,Integer> hashMap = new HashMap<> ();
        hashMap.put(index,0);//从第一张表第一行
        List<List<LinkedHashMap<String, String>>> sheetList= ExcelUtils.importForExcelData(wb,sheets,hashMap,null);
        List<LinkedHashMap<String, String>> rowList =sheetList.get(index-1);
        LinkedHashMap<String,String>rows=rowList.get(0);
        System.out.println(rows);
        if(!(rows.get("0").equals("院部")&&rows.get("1").equals("业绩分计算科室")&&rows.get("2").equals("分类—类别")
                &&rows.get("3").equals("立项年度")&&rows.get("4").equals("项目名称")&&rows.get("5").equals("负责人")
                &&rows.get("6").equals("业绩分（分）"))){

            return JsonData.buildError("请严格按照表模板导入");
        }
        List<PerformanceInfo> performanceInfoList=new ArrayList<>();
        PerformanceInfo p=null;
        for (int i=1;i<rowList.size(); i++) {
            int k=0;
            p=new PerformanceInfo();
            p.setDepartment(rowList.get(i).get(String.valueOf(k++)));
            p.setComputeoffice(rowList.get(i).get(String.valueOf(k++)));
            p.setType(rowList.get(i).get(String.valueOf(k++)));
            p.setYear(rowList.get(i).get(String.valueOf(k++)));
            p.setProject(rowList.get(i).get(String.valueOf(k++)));
            p.setMaster(rowList.get(i).get(String.valueOf(k++)));
            p.setPoints(Integer.parseInt(rowList.get(i).get(String.valueOf(k++))));
            performanceInfoList.add(p);
        }
        boolean res=officeAdminService.insertToPerformanceList(performanceInfoList);
        if(res){
            return JsonData.buildSuccess("导入成功");
        }
        return JsonData.buildError("导入失败");
    }


    /**
     * 科室管理员获取模板
     * @param response
     */
    @PostMapping("/getPerformanceTemplate")
    public void getPerformanceTemplate(HttpServletResponse response){
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工教学业绩分汇总表模板"};
        //设置字段
        String []params=new String [] {"院部","业绩分计算科室","分类-类别","立项年度","项目名称","负责人","业绩分（分）"};
        String []demo = new String[] {"xx学院/处","xx科","xx类别","2019","xxxxx","xxx","100"};
        String []tips = new String [] {"提示：请严格按照报个薄板填写，以免造成数据混乱出错!"};
        List<String[]> a=new ArrayList<> ();
        a.add(params);
        a.add(demo);
        data.add(a);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 科室管理员获取模板
     * @param response
     */
    @PostMapping("/getBonusTemplate")
    public void getBonusTemplate(HttpServletResponse response){
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工教学奖金汇总表模板"};
        //设置字段
        String []params=new String [] {"院部","奖金计算科室（内置）","分类-类别","立项年度","项目名称","负责人","奖金（元）"};
        String []demo = new String[] {"xx学院/处","xx科","xx类别","2019","xxxxx","xxx","100.0"};
        String[]null1=new String[]{""};
        String[]office=new String [] {"内置科室：","教务科", "实验室与设备管理科","团委","质量建设与评估办公室","实践教学科","学籍科"};
        String[]null2=new String[]{"提示：请严格按照报个薄板填写，以免造成数据混乱出错,计算科室请按本模提供的科室来填写"};
        List<String[]> a=new ArrayList<> ();
        a.add(params);
        a.add(demo);
        a.add(null1);
        a.add(office);
        a.add(null2);

        data.add(a);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }


    /**
     * 科室管理员按照模板来导入业绩分数据
     * @param file
     * @param index
     * @return
     */
    @PostMapping("/excelImportBonus")
    public JsonData excelImportBonus(@RequestParam("file") MultipartFile file,@RequestParam(value = "sheetIndex",defaultValue="1") int index){
        ExcelUtils excelUtils=ExcelUtils.initialization();
        Workbook wb = null;
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        InputStream ins1 = null;
        if(file.equals("")||file.getSize()<=0){
            return JsonData.buildError("文件为空");
        }else {
            try {
                ins1 = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 根据后缀名称判断excel的版本
        }
        try {
            if (".xls".equals(extName)) {
                wb = new HSSFWorkbook(ins1);
            } else if (".xlsx".equals(extName)) {
                wb = new XSSFWorkbook(ins1);
            } else {
                // 无效后缀名称，这里之能保证excel的后缀名称，不能保证文件类型正确，不过没关系，在创建Workbook的时候会校验文件格式
                throw new IllegalArgumentException("Invalid excel version");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int num=wb.getNumberOfSheets();
        if(index>num||index<0) return JsonData.buildError("你选择的表有误");
        String[] sheets = new String [num];

        excelUtils.setExpectDateFormatStr("yyyy-MM-dd");
        HashMap<Integer,Integer> hashMap = new HashMap<> ();
        hashMap.put(index,0);//从第一张表第一行
        List<List<LinkedHashMap<String, String>>> sheetList= ExcelUtils.importForExcelData(wb,sheets,hashMap,null);
        List<LinkedHashMap<String, String>> rowList =sheetList.get(index-1);
        LinkedHashMap<String, String>rows=rowList.get(0);
        System.out.println(rows);
        if(!(rows.get("0").trim().equals("院部")&&rows.get("1").trim().equals("奖金计算科室（内置）")&&rows.get("2").trim().equals("分类—类别")
                &&rows.get("3").trim().equals("立项年度")&&rows.get("4").trim().equals("项目名称")&&rows.get("5").trim().equals("负责人")
                &&rows.get("6").trim().equals("奖金（元）"))){
            return JsonData.buildError("请严格按照表表模板导入");
        }
        List<BonusInfo> bonusInfoList=new ArrayList<>();
        BonusInfo p=null;
        for (int i=1;i<rowList.size(); i++) {
            int k=0;
            p=new BonusInfo();
            p.setDepartment(rowList.get(i).get(String.valueOf(k++)));
            p.setComputeoffice(rowList.get(i).get(String.valueOf(k++)));
            p.setType(rowList.get(i).get(String.valueOf(k++)));
            p.setYear(rowList.get(i).get(String.valueOf(k++)));
            p.setProject(rowList.get(i).get(String.valueOf(k++)));
            p.setMaster(rowList.get(i).get(String.valueOf(k++)));
            p.setBonus(Integer.parseInt(rowList.get(i).get(String.valueOf(k++))));
            bonusInfoList.add(p);
        }
        boolean res=officeAdminService.insertToBonusList(bonusInfoList);
        if(res){
            return JsonData.buildSuccess("导入成功");
        }
        return JsonData.buildError("导入失败");
    }


}
