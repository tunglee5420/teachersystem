package com.just.teachersystem.Utill;
import	java.util.Date;
import	java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class YearUtils {
    public static String getSchoolYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM");
        String date=sdf.format(new Date());
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7)) ;
        if(month<9){
            return (year-1) +"-"+year ;
        }else {
            return year+"-"+(year +1);
        }

    }
    public static String getYears(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
        String date=sdf.format(new Date());
        int year = Integer.parseInt(date);
        return String.valueOf(year) ;
    }
}
