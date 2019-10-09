package com.just.teachersystem.Controller;

import com.github.andyczy.java.excel.CommonsUtils;
import com.github.andyczy.java.excel.ExcelUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/a")
public class TestController {
    @RequestMapping("/b")
    public String get(HttpServletResponse response){
        List<List<String[]>> data=new ArrayList<>();
        String[]header=new String [] {"开始","结束"};
        String[]ss1=new String [] {"dbusj","dsjkdn"};
        String[]ss2=new String [] {"dbusj","dsjkdn"};
        List<String[]> a=new ArrayList<> ();
        a.add(header);
        a.add(ss1);
        a.add(ss2);
        data.add(a);

        String[]sheetNameList=new String [] {"今日交易记录"};
        ExcelUtils excelUtils=ExcelUtils.initialization();
        excelUtils.setDataLists(data);
        excelUtils.setSheetName(sheetNameList);
        excelUtils.setFileName("你猜");
        excelUtils.setResponse( response);

        excelUtils.exportForExcelsOptimize();
        System.out.println(111);
        return "bjs";
    }

}
