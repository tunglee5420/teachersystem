package com.just.teachersystem.Utill;
import java.lang.reflect.Array;
import java.util.Arrays;
import	java.util.Date;
import	java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class YearUtils {
    public static String getSchoolYear(String startTime){
//        System.out.println(startTime);
        String []arr=startTime.trim().split("\\D");
        System.out.println(Arrays.toString(arr));
        int year = Integer.parseInt(arr[0]);
//        System.out.println(year);
        int month = Integer.parseInt(arr[1]) ;
//        System.out.println(month);
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
