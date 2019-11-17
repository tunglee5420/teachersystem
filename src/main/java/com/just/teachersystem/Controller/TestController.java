package com.just.teachersystem.Controller;

import java.util.*;


import com.github.andyczy.java.excel.ExcelUtils;

import com.just.teachersystem.Service.CollegeAdminService;
import com.just.teachersystem.Service.OfficeAdminService;
import com.just.teachersystem.Service.RootService;
import com.just.teachersystem.Utill.MyExcelUtil;
import com.just.teachersystem.VO.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/a")
public class TestController {
    @Autowired
    CollegeAdminService collegeAdminService;
    @Autowired
    OfficeAdminService officeAdminService;
    @Autowired
    RootService root;
    @RequestMapping("/b")
    public void get(HttpServletResponse response){
        String year ="2018";
        String department ="教务处";
        PerformanceInfo performanceInfo=new PerformanceInfo();
        performanceInfo.setYear(year);
        performanceInfo.setDepartment(department);
        performanceInfo.setStatus(1);
        List<PerformanceInfo>list=collegeAdminService.confirmPerformance(performanceInfo);


        List<List<String[]>> data=new ArrayList<>();

        ExcelUtils excelUtils=ExcelUtils.initialization();
        String[]labels ={year+"校区、苏理工（"+department+"）教学建设各项业绩分补贴复核表"};
        excelUtils.setLabelName(labels);
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

        HashMap<Integer,Object>regionMap=new HashMap<> () ;
        ArrayList<Integer[]>arrayList = new ArrayList <> ();
        arrayList.add(new Integer[]{list.size()+2,list.size()+2,0,2});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,0,2});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,3,4});
        arrayList.add(new Integer[]{list.size()+3,list.size()+3,5,7});
        regionMap.put(1,arrayList);

        excelUtils.setRegionMap(regionMap);
        excelUtils.setDataLists(data);

        excelUtils.setSheetName(labels);
        excelUtils.setFileName(year+"校区、苏理工（"+department+"）教学建设各项业绩分补贴复核表");

        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();

    }


    @RequestMapping("/c")
    public void getexcel(HttpServletResponse response){
        String year ="2018";
        String department ="船舶与建筑工程学院";
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
            values= new String[]{b.getDepartment(), b.getComputeoffice(),b.getType(),b.getYear(),b.getProject(),b.getMaster(),String.valueOf(b.getBonus())," "};

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

    @GetMapping("/getConstructionExcel")
    public void getConstructionExcel(HttpServletResponse response) {
        String year="2019";
        String schoolYear="2019-2020";

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
     * 科室管理员获取模板
     * @param response
     */
    @GetMapping("/getPerformanceTemplate")
    public void getPerformanceTemplate(HttpServletResponse response){
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
        excelUtils.setResponse( response);
        excelUtils.exportForExcelsOptimize();

    }
    /**
     * 科室管理员获取模板
     * @param response
     */
//    @GetMapping("/getBonusTemplate")
//    public void getBonusTemplate(HttpServletResponse response){
//        List<List<String[]>> data=new ArrayList<>();
//        ExcelUtils excelUtils=ExcelUtils.initialization();
//        //设置表头/表名
//        String[]labels ={"校区、苏理工教学奖金汇总表模板"};
//        //设置字段
//        String []params=new String [] {"院部","奖金计算科室（内置）","分类-类别","立项年度","项目名称","负责人","奖金（元）"};
//        String []demo = new String[] {"xx学院/处","xx科","xx类别","2019","xxxxx","xxx","100.0"};
//        String[]null1=new String[]{};
//        String[]office=new String [] {"内置科室：","教务科", "实验室与设备管理科","团委","质量建设与评估办公室","实践教学科","学籍科"};
//
//        List<String[]> a=new ArrayList<> ();
//        a.add(params);
//        a.add(demo);
//        a.add(null1);
//        a.add(null1);
//        a.add(office);
//        data.add(a);
//        excelUtils.setDataLists(data);
//        excelUtils.setSheetName(labels);
//        excelUtils.setFileName(labels[0]);
//        excelUtils.setResponse( response);
//        excelUtils.exportForExcelsOptimize();
//    }

//    /**
//     * 下载用户表
//     */
//    @GetMapping("/getUserExcel")
//    public void getUserExcel(HttpServletResponse response) {
//        UserInfo userInfo = new UserInfo();
//        List<UserInfo>list=root.getUserInfo(userInfo);
//
//
//
//        List<List<String[]>> data=new ArrayList<>();
//        ExcelUtils excelUtils=ExcelUtils.initialization();
//        //设置表头/表名
//        String[]labels ={"校区、苏理工教职工人信息表"};
//        //excelUtils.setLabelName(labels);
//        //设置字段
//        String []params=new String [] {"部门","姓名","工号","性别","出生年月",
//                "入校时间","电话","专业技术职称","最高学历","最高学位","授学位单位名称","获最高学位的专业名称","是否双师型",
//                "是否具有行业背景","是否博硕士生导师"};
//        excelUtils.setLabelName(labels);
//        List<String[]> a=new ArrayList<> ();
//        a.add(params);
//
//        String []values=null;
//
//        for (UserInfo p:list) {
//
//            values= new String[]{p.getDptname(), p.getName(),p.getWorknum(),p.getGender(), String.valueOf(p.getBirthday()),
//                    String.valueOf(p.getEnterTime()),p.getPhone(),p.getTechTittle(),p.getEduBgd(),p.getDegree(),p.getSchool(),
//                    p.getMajor(),p.getDoubleTeacher()==0?"不是":"是", p.getBackground()==0?"不是":"是", p.getTutor()==0?"不是":"是"};
//
//            a.add(values);
//        }
//
//        data.add(a);
//        excelUtils.setDataLists(data);
//        excelUtils.setSheetName(labels);
//        excelUtils.setFileName(labels[0]);
//        excelUtils.setResponse( response);
//        excelUtils.exportForExcelsOptimize();
//    }
}
