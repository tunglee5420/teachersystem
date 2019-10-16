package com.just.teachersystem.Controller;
import java.util.*;


import com.github.andyczy.java.excel.ExcelUtils;
import com.just.teachersystem.Service.CollegeAdminService;
import com.just.teachersystem.VO.BonusInfo;
import com.just.teachersystem.VO.PerformanceInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/a")
public class TestController {
    @Autowired
    CollegeAdminService collegeAdminService;
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



}
