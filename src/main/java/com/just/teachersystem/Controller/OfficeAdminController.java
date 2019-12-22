package com.just.teachersystem.Controller;

import java.sql.Date;
import java.util.*;
import com.github.andyczy.java.excel.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.teachersystem.Annotation.Logs;
import com.just.teachersystem.Entity.Award;
import com.just.teachersystem.Exception.MyException;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Service.OfficeAdminService;
import com.just.teachersystem.Utill.JsonData;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.Utill.MyExcelUtil;
import com.just.teachersystem.Utill.YearUtils;
import com.just.teachersystem.VO.*;
import io.jsonwebtoken.Claims;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import javax.swing.*;


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
     * department 部门
     * class3 类别
     * level 级别
     *  year 年度
     * schoolYear 学年
     * page
     * size
     * @return
     */
    @Logs(role="officeAdmin",description = "获取用户提交的建设类信息")
    @PostMapping("/getUserConstruction")
    public JsonData getUserConstruction(@RequestHeader Map<String ,String> header,
                                        @RequestBody ConstructionInfo construction,
                                        @RequestParam(value = "page",defaultValue = "1") int page,
                                        @RequestParam(value = "size",defaultValue = "20")int size
                                        ){
        PageHelper.startPage(page,size);
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String dpt=(String) claims.get("department");
        if(!dpt.equals("质量建设与评估办公室")){
            return JsonData.buildError("你没有权限");
        }
        if(construction==null){
            return JsonData.buildError("参数出错");
        }

        List<ConstructionInfo> list=officeAdminService.getUserConstruction(construction);

        PageInfo<ConstructionInfo> pageInfo=new PageInfo<> (list);

        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 导出建设类excel
     *  header
     *  year 年度
     *  schoolYear 学年
     * @param response
     */
    @Logs(role="officeAdmin",description = "导出建设类excel")
    @PostMapping("/getConstructionExcel")
    public void getConstructionExcel(@RequestHeader Map<String, String> header,
                                     @RequestBody ConstructionInfo construction,
                                     HttpServletResponse response) {
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw  new MyException("你没有权限");
        }
        if(construction==null){
            throw  new MyException("参数异常");
        }
        List<ConstructionInfo> list=officeAdminService.getUserConstruction(construction);

        if(list==null){
            throw new MyException("内部异常");
        }
        //设置表格
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={construction.getYear()+"校区、苏理工教学建设各项业绩分表"};
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
                    p.getIsEnd()==0?"否":"是",p.getSchoolYear(),p.getYear()};

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
     * 导出建设类证明材料的key
     * @param header
     * year 年度
     * schoolYear 学年
     */
    @Logs(role="officeAdmin",description = "导出建设类证明材料的key")
    @PostMapping("/getConstructionFileKey")
    public JsonData getConstructionFileKey(@RequestHeader Map<String, String> header,
                                           @RequestBody ConstructionInfo construction) {
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String department = (String) claims.get("department");
        if (!department.equals("质量建设与评估办公室")) {
            throw new MyException("你没有权限");
        }
        if(construction==null){
            throw  new MyException("参数异常");
        }
        List<ConstructionInfo> list = officeAdminService.getUserConstruction(construction);
        if(list==null){
            return JsonData.buildError("数据错误");
        }
        Map<String,String> map ;
        List<Map> fileList=new ArrayList<>();
        for (ConstructionInfo a:list) {
            map = new HashMap<> ();
            map.put("name" ,a.getName()+"-"+a.getProject());
            map.put("files",a.getTestimonial());
            fileList.add(map);

        }
        return JsonData.buildSuccess(fileList);
    }


    /**
     * 科室管理员获取建设类模板
     * @param response
     */
    @Logs(role="officeAdmin",description = "科室管理员获取建设类模板")
    @PostMapping("/getConstructionTemplate")
    public void getConstructionTemplate(HttpServletResponse response,@RequestHeader Map<String, String> header){

        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw  new MyException("你没有权限");
        }

        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工建设类信息表模板"};
        //设置字段
        String []params=new String [] {"项目编号","院部","项目名称","负责人","工号","课题组成员","立项时间","项目起止年月","主办单位","分类","类别","级别","佐证材料","建设经费（元）","业绩分","业绩分计算年度",
                "奖金（元）","归档文件盒编号","是否结束","学年","年度"};
        String []demo = new String[] {"SJYZ20180701","船舶与建筑工程学院","以微信公众平台为XX","孟XX","999XXX21979","方XX","2019/5/1","2019/5-2020/5","江苏科技大学","课程建设",
                "教改示范课程","江科大","","6000","545","2019",
                "500","XXX","是","2018-2019","2019"};
        List<List<String[]>> data=MyExcelUtil.getExcelTempleData(labels,"提示：请严格按照这个模板填写，以免造成数据混乱出错!（使用时请删除）",params,demo,null);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 科室管理员按照模板来导入建设类数据
     * @param file
     * @param index
     * @return
     */
    @Logs(role="officeAdmin",description = "科室管理员按照模板来导入建设类数据")
    @PostMapping("/excelImportConstruction")
    public JsonData excelImportConstruction(@RequestHeader Map<String, String> header,@RequestParam("file") MultipartFile file,@RequestParam(value = "sheetIndex",defaultValue="1") int index){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw  new MyException("你没有权限");
        }

        ExcelUtils excelUtils=ExcelUtils.initialization();
        Workbook wb = MyExcelUtil.check(file);

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
        if(!(rows.get("0").equals("项目编号")&&rows.get("1").equals("院部")&&rows.get("2").equals("项目名称")&&rows.get("3").equals("负责人")
                &&rows.get("4").equals("工号")&&rows.get("5").equals("课题组成员")&&rows.get("6").equals("立项时间")
                &&rows.get("7").equals("项目起止年月")&&rows.get("8").equals("主办单位")&&rows.get("9").equals("分类")
                &&rows.get("10").equals("类别")&&rows.get("11").equals("级别") &&rows.get("12").equals("佐证材料")
                &&rows.get("13").equals("建设经费（元）")&&rows.get("14").equals("业绩分")&&rows.get("15").equals("业绩分计算年度")
                &&rows.get("16").equals("奖金（元）")&&rows.get("17").equals("归档文件盒编号")&&rows.get("18").equals("是否结束")
                &&rows.get("19").equals("学年")&&rows.get("20").equals("年度"))){
            return JsonData.buildError("请严格按照表模板导入");
        }
        List<ConstructionInfo> constructionInfoList=new ArrayList<>();
        ConstructionInfo con=null;
        for (int i=1;i<rowList.size(); i++) {
            int k=0;
            con=new ConstructionInfo();
            Map<String,String> map= rowList.get(i);//获取第几行表格

            con.setProjectNum(map.get(String.valueOf(k++)));//项目编号
            con.setDepartment(map.get(String.valueOf(k++)));//院部
            con.setProject(map.get(String.valueOf(k++)));//项目名称
            con.setName(map.get(String.valueOf(k++)));//负责人
            con.setWorknum(map.get(String.valueOf(k++)));//工号
            con.setTeammate(map.get(String.valueOf(k++)));//课题组成员
            con.setStartTime(map.get(String.valueOf(k++)));//立项时间
            con.setBeginToEndTime(map.get(String.valueOf(k++)));//项目起止年月
            con.setSponsor(map.get(String.valueOf(k++)));//主办单位
            con.setClass2(map.get(String.valueOf(k++)));//分类
            con.setClass3(map.get(String.valueOf(k++)));//类别
            con.setLevel(map.get(String.valueOf(k++)));//级别
            con.setTestimonial(map.get(String.valueOf(k++)));//佐证材料
            con.setExpenditure(Double.parseDouble(map.get(String.valueOf(k++)).isEmpty() ?"0":rowList.get(i).get(String.valueOf(k-1))));//建设经费（元）
            con.setPoint(Long.parseLong(map.get(String.valueOf(k++)).isEmpty()  ?"0":rowList.get(i).get(String.valueOf(k-1))));//业绩分

            con.setComputeYear(map.get(String.valueOf(k++)));//业绩分计算年度

            con.setBonus(Double.parseDouble(map.get(String.valueOf(k++)).isEmpty() ?"0":rowList.get(i).get(String.valueOf(k-1))));//奖金（元）

            con.setFileNumber(map.get(String.valueOf(k++)));//归档文件盒编号
            con.setStatus(0);
            con.setIsEnd(0);
            con.setYear(YearUtils.getYears(con.getStartTime()));
            con.setSchoolYear(YearUtils.getSchoolYear(con.getStartTime()));
            constructionInfoList.add(con);
        }
        boolean res=officeAdminService.insertToConstruction(constructionInfoList);
        if(res){
            return JsonData.buildSuccess("导入成功");
        }
        return JsonData.buildError("导入失败");
    }

    /**
     *核审 ,补充,修改建设信息
     * @param header
     * @param construction
     * @return
     */
    @Logs(role="officeAdmin",description = "补充修改建设信息并核审")
    @PostMapping("/constructionSupplement")
    public JsonData constructionSupplement(@RequestHeader Map<String ,String> header,@RequestBody ConstructionInfo construction){
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
     *  department部门
     *  class3 类别
     *  year 年度
     *  schoolYear 学年
     * @param page
     * @param size
     * @return
     */
    @Logs(role="officeAdmin",description = "获取用户提交的成果类信息")
    @PostMapping("/getUserAchievement")
    public JsonData getUserAchievement(@RequestHeader Map<String ,String> header,
                                       @RequestBody AchievementInfo achievement,

                                       @RequestParam(value = "page",defaultValue = "1") int page,
                                       @RequestParam(value = "size",defaultValue = "30")int size) {
        PageHelper.startPage(page,size);
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String dpt = (String) claims.get("department");
        if (!dpt.equals("质量建设与评估办公室")) {
            return JsonData.buildError("你没有权限");
        }
       if(achievement==null) return JsonData.buildError("参数异常");
        List<AchievementInfo> list=officeAdminService.getUserAchievement(achievement);

        PageInfo<AchievementInfo> pageInfo=new PageInfo<> (list);

        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 导出成果类excel
     * @param header
     *  year 学年
     *  schoolYear 年度
     *  response
     */
    @Logs(role="officeAdmin",description = "导出成果类excel")
    @PostMapping("/getAchievementExcel")
    public void getAchievementExcel(@RequestHeader Map<String, String> header,
                                    @RequestBody AchievementInfo achievement,
                                    HttpServletResponse response) {
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String department = (String) claims.get("department");
        if (!department.equals("质量建设与评估办公室")) {
            throw  new MyException("你没有权限");
        }
        if(achievement==null)  throw  new MyException("参数异常");
        List<AchievementInfo> list=officeAdminService.getUserAchievement(achievement);

        if(list==null){
            throw new MyException("内部错误");
        }
        //设置表格
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={achievement.getYear()+"校区、苏理工成果类表"};
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
     * 获取成果类文件key
     * @param header
     *  year 年度
     *  schoolYear 学年
     * @return
     */
    @Logs(role="officeAdmin",description = "获取成果类文件key")
    @PostMapping("/getAchievementFileKey")
    public JsonData getAchievementFileKey(@RequestHeader Map<String, String> header,
                                          @RequestBody AchievementInfo achievement){

        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String department = (String) claims.get("department");
        if (!department.equals("质量建设与评估办公室")) {
            return JsonData.buildError("你没有权限");
        }

        if(achievement==null) return JsonData.buildError("参数异常");
        List<AchievementInfo> list=officeAdminService.getUserAchievement(achievement);
        if(list==null){
            return JsonData.buildError("数据错误");
        }



        Map<String,String> map ;
        List<Map> fileList=new ArrayList<>();

        for (AchievementInfo a:list) {
            map = new HashMap<> ();
            map.put("name" ,a.getName()+"-"+a.getProduction());
            map.put("files",a.getCertificate());
            fileList.add(map);
        }
        return JsonData.buildSuccess(fileList);
    }


    /**
     * 科室管理员获取成果类模板
     * @param response
     */
    @Logs(role="officeAdmin",description = "科室管理员获取成果类模板")
    @PostMapping("/getAchievementTemplate")
    public void getAchievementTemplate(HttpServletResponse response,@RequestHeader Map<String, String> header){

        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw  new MyException("你没有权限");
        }

        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工成果类信息表模板"};
        //设置字段
        String []params=new String [] {"院部名称","第一作者","工号","组员","成果名称","成果类别","级别","发表刊物/出版社/授权单位","发表时间/出版时间/授权时间（年/月）","是否被转让或采用（仅限于专利）","证书","学年","年度"};
        String []demo = new String[] {"公共教育学院","孟XX","999XXXXX979","方XX","第X届“蓝桥杯”全国软件和信息技术专业人才大赛省赛JavaB组","学术论文","SCI","全国教育科学规划领导小组办公室",
                "2018/5/1","是","","2018-2019","2019"};
        List<List<String[]>> data=MyExcelUtil.getExcelTempleData(labels,"提示：请严格按照这个模板填写，以免造成数据混乱出错!（使用时请删除）",params,demo,null);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }


    /**
     * 科室管理员按照模板来导入成果类数据
     * @param file
     * @param index
     * @return
     */
    @Logs(role="officeAdmin",description = "科室管理员按照模板来导入成果类数据")
    @PostMapping("/excelImportAchievement")
    public JsonData excelImportAchievement(@RequestHeader Map<String, String> header,@RequestParam("file") MultipartFile file,@RequestParam(value = "sheetIndex",defaultValue="1") int index){
        //校验权限
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw  new MyException("你没有权限");
        }
        //导excel
        ExcelUtils excelUtils=ExcelUtils.initialization();
        Workbook wb = MyExcelUtil.check(file);

        int num=wb.getNumberOfSheets();

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

        if(!(rows.get("0").equals("院部名称")&&rows.get("1").equals("第一作者")&&rows.get("2").equals("工号")&&rows.get("3").equals("组员")
                &&rows.get("4").equals("成果名称")&&rows.get("5").equals("成果类别")&&rows.get("6").equals("级别") &&rows.get("7").equals("发表刊物/出版社/授权单位")
                &&rows.get("8").equals("发表时间/出版时间/授权时间（年/月）")&&rows.get("9").equals("是否被转让或采用（仅限于专利）")
                &&rows.get("10").equals("证书")&&rows.get("11").equals("学年")&&rows.get("12").equals("年度"))){
            return JsonData.buildError("请严格按照表模板导入");
        }
        List<AchievementInfo> achievementInfoList=new ArrayList<>();
        AchievementInfo ach=null;

        for (int i=1;i<rowList.size(); i++) {
            int k=0;

            ach=new AchievementInfo();
            Map<String,String> map= rowList.get(i);//获取第几行表格
            ach.setDepartment(map.get(String.valueOf(k++)));//院部
            ach.setName(map.get(String.valueOf(k++)));//第一作者
            ach.setWorknum(map.get(String.valueOf(k++)));//工号
            ach.setTeammate(map.get(String.valueOf(k++)));//课题组成员
            ach.setProduction(map.get(String.valueOf(k++)));//成果名称
            ach.setClass2(map.get(String.valueOf(k++)));//成果类别
            ach.setClass3(map.get(String.valueOf(k++)));//级别
            ach.setUnit(map.get(String.valueOf(k++)));//出版单位
            ach.setPublishTime(map.get(String.valueOf(k++)));//发表时间/出版时间/授权时间（年/月）
            ach.setPatent(map.get(String.valueOf(k++)).equals("是")?1:0);//是否被转让或采用（仅限于专利）
            ach.setCertificate(map.get(String.valueOf(k++)));
            ach.setStatus(0);
            ach.setYear(YearUtils.getYears(String.valueOf(ach.getPublishTime())));
            ach.setSchoolYear(YearUtils.getSchoolYear(String.valueOf(ach.getPublishTime())));
            System.out.println(ach);
            achievementInfoList.add(ach);
        }
        boolean res=officeAdminService.insertToAchievement(achievementInfoList);
        if(res){
            return JsonData.buildSuccess("导入成功");
        }
        return JsonData.buildError("导入失败");
    }


    /**
     * 核审,补充,修改成果信息
     * @param header
     * @param info
     * @return
     */
    @Logs(role="officeAdmin",description = "补充修改成果信息并核审")
    @PostMapping("/achievementSupplement")
    public JsonData achievementSupplement(@RequestHeader Map<String, String> header,
                                          @RequestBody AchievementInfo info) {
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
     * department 部门
     * class3 类别
     * level 级别
     * year 年度
     * prize 奖项
     * schoolYear 学年
     * @param page
     * @param size
     * @return
     */
    @Logs(role="officeAdmin",description = "获取用户提交的获奖类信息")
    @PostMapping("/getUserAward")
    public JsonData getUserAward(@RequestHeader Map<String ,String> header,
                                 @RequestBody  AwardInfo awardInfo,

                                 @RequestParam(value = "page",defaultValue = "1") int page,
                                 @RequestParam(value = "size",defaultValue = "30") int size ){
        PageHelper.startPage(page,size);
        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String dpt = (String) claims.get("department");
        if (!dpt.equals("质量建设与评估办公室")) {
            return JsonData.buildError("你没有权限");
        }
        if(awardInfo==null) return JsonData.buildError("参数异常");
        List<AwardInfo> list=officeAdminService.getUserAward(awardInfo);

        PageInfo<AwardInfo> pageInfo=new PageInfo<> (list);

        return JsonData.buildSuccess(pageInfo);

    }

    /**
     * 科室管理员获取获奖类模板
     * @param response
     */
    @Logs(role="officeAdmin",description = "科室管理员获取获奖类模板")
    @PostMapping("/getAwardTemplate")
    public void getAwardTemplate(HttpServletResponse response,@RequestHeader Map<String, String> header){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw  new MyException("你没有权限");
        }

        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工获奖类信息表模板"};
        //设置字段
        String []params=new String [] {"院部名称","获奖教师（排名第1）","工号","获奖成员","获奖内容","分类","类别","级别","奖项","颁奖部门","奖金计算年度","奖金（元）","证书","获奖时间","学年","年度"};
        String []demo = new String[] {"XX学院","孟XX","999XXXXX979","方XX","第五届“浩辰杯”A华东区大学生CAD应用技能竞赛","教师获奖","教学成果奖","苏理工",
                "特等奖","苏州理工学院","2018","2000","","获奖时间","2018-2019","2019"};
        List<List<String[]>> data=MyExcelUtil.getExcelTempleData(labels,"提示：请严格按照这个模板填写，以免造成数据混乱出错!（使用时请删除）",params,demo,null);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse(response);
        excelUtils.exportForExcelsOptimize();
    }



    /**
     * 科室管理员按照模板来导入获奖类数据
     * @param file
     * @param index
     * @return
     */
    @Logs(role="officeAdmin",description = "科室管理员按照模板来导入获奖类数据")
    @PostMapping("/excelImportAward")
    public JsonData excelImportAward(@RequestHeader Map<String, String> header,@RequestParam("file") MultipartFile file,@RequestParam(value = "sheetIndex",defaultValue="1") int index){
        //校验权限
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw  new MyException("你没有权限");
        }

        //导excel
        ExcelUtils excelUtils=ExcelUtils.initialization();
        Workbook wb = MyExcelUtil.check(file);

        int num=wb.getNumberOfSheets();

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

        if(!(rows.get("0").equals("院部名称")&&rows.get("1").equals("获奖教师（排名第1）")&&rows.get("2").equals("工号")&&rows.get("3").equals("获奖成员")
                &&rows.get("4").equals("获奖内容")&&rows.get("5").equals("分类")&&rows.get("6").equals("类别") &&rows.get("7").equals("级别")
                &&rows.get("8").equals("奖项")&&rows.get("9").equals("颁奖部门") &&rows.get("10").equals("奖金计算年度")&&rows.get("11").equals("奖金（元）")
                &&rows.get("12").equals("证书")&&rows.get("13").equals("获奖时间")&&rows.get("14").equals("学年")&&rows.get("15").equals("年度"))){
            return JsonData.buildError("请严格按照表模板导入");
        }
        List<AwardInfo> awardInfoList=new ArrayList<>();
        AwardInfo awd=null;

        for (int i=1;i<rowList.size(); i++) {
            int k=0;
            awd=new AwardInfo();
            Map<String,String> map= rowList.get(i);//获取第几行表格
            awd.setDepartment(map.get(String.valueOf(k++)));//院部
            awd.setName(map.get(String.valueOf(k++)));//获奖教师（排名第1）
            awd.setWorknum(map.get(String.valueOf(k++)));//工号
            awd.setTeammate(map.get(String.valueOf(k++)));//获奖成员
            awd.setContent(map.get(String.valueOf(k++)));//获奖内容
            awd.setClass2(map.get(String.valueOf(k++)));//分类
            awd.setClass3(map.get(String.valueOf(k++)));//类别
            awd.setLevel(map.get(String.valueOf(k++)));//级别
            awd.setPrize(map.get(String.valueOf(k++)));//奖项
            awd.setAwardUnit(map.get(String.valueOf(k++)));//颁奖部门
            awd.setAwardYear(map.get(String.valueOf(k++)));//奖金计算年度
            awd.setBonus(Double.parseDouble(map.get(String.valueOf(k++))));//奖金（元）
            awd.setCertificate(map.get(String.valueOf(k++)));//证书
            awd.setAwardTime(map.get(String.valueOf(k++)));//获奖时间
            awd.setSchoolYear(YearUtils.getSchoolYear(String.valueOf(awd.getAwardTime())));//学年
            awd.setYear(YearUtils.getYears(String.valueOf(awd.getAwardTime())));//年度
            awardInfoList.add(awd);
            System.out.println(awd);
        }
        boolean res=officeAdminService.insertToAward(awardInfoList);
        if(res){
            return JsonData.buildSuccess("导入成功");
        }
        return JsonData.buildError("导入失败");
    }


    /**
     *核审,补充,修改获奖类信息
     * @param header
     * @param awardInfo
     * @return
     */
    @Logs(role="officeAdmin",description = "补充修改获奖类信息并核审")
    @PostMapping("/awardSupplement")
    public JsonData awardSupplement(@RequestHeader Map<String, String> header, @RequestBody AwardInfo awardInfo) {
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
     *  year 年度
     *  schoolYear 学年
     * @param response
     */
    @Logs(role="officeAdmin",description = "获取获奖类excel")
    @PostMapping("/getAwardExcel")
    public void getAwardExcel(@RequestHeader Map<String ,String> header,
                              @RequestBody AwardInfo awardInfo,
                              HttpServletResponse response){
        String token=header.get("token");
        Claims claims =JwtUtils.checkJWT(token);
        String department=(String) claims.get("department");
        if(!department.equals("质量建设与评估办公室")){
            throw new MyException("你暂无权限");
        }

        List<AwardInfo>list=officeAdminService.getUserAward(awardInfo);
        if(list==null){
            throw new MyException("内部错误");
        }
        List<List<String[]>> data=new ArrayList<>();
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={awardInfo.getYear()+"校区、苏理工教学获奖表"};
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
     * 获取获奖类文件key
     * @param header
     * year 年度
     * schoolYear 学年
     * @return
     */
    @Logs(role="officeAdmin",description = "获取获奖类文件key")
    @PostMapping("/getAwardFileKey")
    public JsonData getAwardFileKey(@RequestHeader Map<String, String> header,
                                    @RequestBody AwardInfo awardInfo){

        String token = header.get("token");
        Claims claims = JwtUtils.checkJWT(token);
        String department = (String) claims.get("department");
        if (!department.equals("质量建设与评估办公室")) {
            throw  new MyException("你没有权限");
        }

        if(awardInfo==null) return JsonData.buildError("参数异常");
        List<AwardInfo>list=officeAdminService.getUserAward(awardInfo);
        if(list==null){
            return JsonData.buildError("数据错误");
        }

        Map<String,String> map ;
        List<Map> fileList=new ArrayList<>();
        for (AwardInfo a:list) {
            map = new HashMap<> ();
            map.put("name" ,a.getName()+"-"+a.getContent());
            map.put("files",a.getCertificate());
            fileList.add(map);
        }
        return JsonData.buildSuccess(fileList);
    }


    /**
     * 科室管理员按照模板来导入业绩分数据
     * @param file
     * @param index
     * @return
     */
    @Logs(role="officeAdmin",description = "科室管理员按照模板来导入业绩分数据")
    @PostMapping("/excelImportPerformance")
    public JsonData excelImportPerformance(@RequestParam("file") MultipartFile file,@RequestParam(value = "sheetIndex",defaultValue="1") int index){
        ExcelUtils excelUtils=ExcelUtils.initialization();
        Workbook wb = MyExcelUtil.check(file);

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
//        System.out.println(rows);
        if(!(rows.get("0").equals("院部")&&rows.get("1").equals("业绩分计算科室")&&rows.get("2").equals("分类-类别")
                &&rows.get("3").equals("立项年度")&&rows.get("4").equals("项目名称")&&rows.get("5").equals("负责人")
                &&rows.get("6").equals("业绩分（分）"))){

            return JsonData.buildError("请严格按照表模板导入");
        }
        List<PerformanceInfo> performanceInfoList=new ArrayList<>();
        PerformanceInfo p=null;
        for (int i=1;i<rowList.size(); i++) {
            int k=0;
            p=new PerformanceInfo();
            Map<String,String> map= rowList.get(i);//获取第几行表格
            p.setDepartment(map.get(String.valueOf(k++)));
            p.setComputeoffice(map.get(String.valueOf(k++)));
            p.setType(map.get(String.valueOf(k++)));
            p.setYear(map.get(String.valueOf(k++)));
            p.setProject(map.get(String.valueOf(k++)));
            p.setMaster(map.get(String.valueOf(k++)));
            p.setPoints(Integer.parseInt(map.get(String.valueOf(k++)).isEmpty()?"0":map.get(String.valueOf(k-1))));
            performanceInfoList.add(p);
        }
        boolean res=officeAdminService.insertToPerformanceList(performanceInfoList);
        if(res){
            return JsonData.buildSuccess("导入成功");
        }
        return JsonData.buildError("导入失败");
    }


    /**
     * 科室管理员获取业绩模板
     * @param response
     */
    @Logs(role="officeAdmin",description = "科室管理员获取业绩模板")
    @PostMapping("/getPerformanceTemplate")
    public void getPerformanceTemplate(HttpServletResponse response){

        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工教学业绩分汇总表模板"};
        //设置字段
        String []params=new String [] {"院部","业绩分计算科室","分类-类别","立项年度","项目名称","负责人","业绩分（分）"};
        String []demo = new String[] {"xx学院/处","xx科","xx类别","2019","xxxxx","xxx","100"};
        List<List<String[]>> data=MyExcelUtil.getExcelTempleData(labels,"请严格按照这个模板填写，以免造成数据混乱出错!（使用时请删除）",params,demo,null);
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }

    /**
     * 科室管理员获取奖金模板
     * @param response
     */
    @Logs(role="officeAdmin",description = "科室管理员获取奖金模板")
    @PostMapping("/getBonusTemplate")
    public void getBonusTemplate(HttpServletResponse response){
        ExcelUtils excelUtils=ExcelUtils.initialization();
        //设置表头/表名
        String[]labels ={"校区、苏理工教学奖金汇总表模板"};
        //设置字段
        String []params=new String [] {"院部","奖金计算科室（内置）","分类-类别","立项年度","项目名称","负责人","奖金（元）"};
        String []demo = new String[] {"xx学院/处","xx科","xx类别","2019","xxxxx","xxx","100.0"};

        String[]office=new String [] {"内置科室：","教务科", "实验室与设备管理科","团委","质量建设与评估办公室","实践教学科","学籍科使用时请删除）"};

        List<List<String[]>> data=MyExcelUtil.getExcelTempleData(labels,
                "提示：请严格按照报个薄板填写，以免造成数据混乱出错,计算科室请按本模提供的科室来填写（使用时请删除）",params,demo, office);

        excelUtils.setDataLists(data);
        excelUtils.setSheetName(labels);
        excelUtils.setFileName(labels[0]);
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();
    }


    /**
     * 科室管理员按照模板来导入奖金数据
     * @param file
     * @param index
     * @return
     */
    @Logs(role="officeAdmin",description = "科室管理员按照模板来导入奖金数据")
    @PostMapping("/excelImportBonus")
    public JsonData excelImportBonus(@RequestParam("file") MultipartFile file,@RequestParam(value = "sheetIndex",defaultValue="1") int index){
        ExcelUtils excelUtils=ExcelUtils.initialization();
        Workbook wb =MyExcelUtil.check(file);//检验文件类型
        int num=wb.getNumberOfSheets();
        if(index>num||index<0) return JsonData.buildError("你选择的表有误");
        String[] sheets = new String [num];

        excelUtils.setExpectDateFormatStr("yyyy-MM-dd");
        HashMap<Integer,Integer> hashMap = new HashMap<> ();
        hashMap.put(index,0);//从第一张表第一行
        List<List<LinkedHashMap<String, String>>> sheetList= ExcelUtils.importForExcelData(wb,sheets,hashMap,null);
        List<LinkedHashMap<String, String>> rowList =sheetList.get(index-1);
        LinkedHashMap<String, String>rows=rowList.get(0);
//        System.out.println(rows);

        if(!(rows.get("0").trim().equals("院部")&&rows.get("1").trim().equals("奖金计算科室（内置）")&&rows.get("2").trim().equals("分类-类别")
                &&rows.get("3").trim().equals("立项年度")&&rows.get("4").trim().equals("项目名称")&&rows.get("5").trim().equals("负责人")
                &&rows.get("6").trim().equals("奖金（元）"))){
            return JsonData.buildError("请严格按照表表模板导入");
        }
        List<BonusInfo> bonusInfoList=new ArrayList<>();
        BonusInfo p=null;
        System.out.println(rowList.size());
        for (int i=1;i<rowList.size(); i++) {
            int k=0;
            p=new BonusInfo();


            Map<String,String> map= rowList.get(i);//获取第几行表格
            p.setDepartment(map.get(String.valueOf(k++)));
            p.setComputeoffice(map.get(String.valueOf(k++)));
            p.setType(map.get(String.valueOf(k++)));
            p.setYear(map.get(String.valueOf(k++)));
            p.setProject(map.get(String.valueOf(k++)));
            p.setMaster(map.get(String.valueOf(k++)));

            p.setBonus(Double.parseDouble(map.get(String.valueOf(k++)).isEmpty()?"0":map.get(String.valueOf(k-1))));
            System.out.println(p.getBonus());
            bonusInfoList.add(p);
        }
        boolean res=officeAdminService.insertToBonusList(bonusInfoList);
        if(res){
            return JsonData.buildSuccess("导入成功");
        }
        return JsonData.buildError("导入失败");
    }


}
