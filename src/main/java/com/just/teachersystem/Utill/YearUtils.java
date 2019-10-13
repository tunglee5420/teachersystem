package com.just.teachersystem.Utill;
import	java.util.Date;
import	java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class YearUtils {
    public static String getSchoolYear(String startTime){

        int year = Integer.parseInt(startTime.substring(0,4));
        int month = Integer.parseInt(startTime.substring(5,7)) ;
        if(month<9){
            return (year-1) +"-"+year ;
        }else {
            return year+"-"+(year +1);
        }

    }
    public static String getYears(String startTime){
        int year = Integer.parseInt(startTime.substring(0,4));
        return String.valueOf(year) ;
    }
}
