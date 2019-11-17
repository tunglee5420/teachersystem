package com.just.teachersystem.Utill;




import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class PropertiesUtil {
    /**
         * 读取properties文件
         * @param resource 文件名称
         * @return
         */
    public static Properties getProperties(String resource){

        Properties properties = new Properties();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            properties.load(is);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        }
        return properties;
    }
    /**
     * 获取property
     * @param key key
     * @return value
     */
    public static String getProperty(String resource,String key){
        Properties properties = PropertiesUtil.getProperties(resource);
        return properties.getProperty(key);
    }

    /**
      * 获取getPropertyNew
      * @param key key
      * @return value
      */
    public static String getPropertyNew(String resource,String key){
        Properties properties = new Properties();
        try {
            InputStreamReader is = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource), "UTF-8");
            properties.load(is);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        }
        return properties.getProperty(key);
    }

    /**
     * 获取匹配的属性列表
     * @param resource 资源文件名称
     * @param prefix 匹配的前缀字符串
     * @return 匹配的结果存入List
     */
    public static List getMatchProperties4List(String resource,String prefix){
        Properties properties = PropertiesUtil.getProperties(resource);
        List list = new ArrayList();
        Iterator it = properties.entrySet().iterator();
        Object key = null;
        Object value = null;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            key = entry.getKey();
            if(key.toString().startsWith(prefix)){
                value = entry.getValue();
                list.add(value);
            }
        }
        return list;
    }

}