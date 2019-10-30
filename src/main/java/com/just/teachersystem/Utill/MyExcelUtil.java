package com.just.teachersystem.Utill;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyExcelUtil {

    /**
     *
     * @param labels  文件名/表头
     * @param tip 提示
     * @param params 表格字段
     * @param demo 样例数据
     * @param other 其他提示
     * @return
     */
    public  static List<List<String[]>>  getExcelTempleData( String []labels, String tip,String []params,
                                                             String []demo, String[] other){
        List<List<String[]>> data=new ArrayList<>();
        List<String[]> a=new ArrayList<> ();
        //设置表头/表名

        //设置字段
        if(params!=null)  a.add(params);
        if(demo!=null) a.add(demo);
        String[]null1=new String[]{""};
        a.add(null1);
        a.add(null1);

        String[]tips=new String[]{tip};
        a.add(null1);
        if(other!=null) a.add(other);
        a.add(tips);
        data.add(a);
        return data;
    }

    public  static Workbook check(MultipartFile file){
        Workbook wb = null;
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        InputStream ins1 = null;
        if(file.equals("")||file.getSize()<=0){
            return null;
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

            return wb;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
