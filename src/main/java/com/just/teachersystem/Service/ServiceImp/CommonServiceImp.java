package com.just.teachersystem.Service.ServiceImp;
import java.util.*;

import com.just.teachersystem.Entity.Department;
import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.Mapper.CommonMapper;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.Utill.MyException;
import com.just.teachersystem.Utill.RedisUtils;
import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 常见通用服务实现层
 */
@Service
@Component
public class CommonServiceImp  implements CommonService {
    @Autowired
    CommonMapper mapper;

    @Autowired
    RedisUtils redisUtils;


    /**
     * 登陆服务
     * @param worknum
     * @param password
     * @return
     */
    public Map<String, Object> login(String worknum, String password){
        if(worknum==null || password == null){
            return null;
        }
//        System.out.println(worknum);
        UserInfo user=mapper.getInfo(worknum);
        //System.out.println(user);
        String token=JwtUtils.creatJwt(user);
//        System.out.println(token);
        Map<String, Object> map = new HashMap<> ();
        if(password.equals(user.getPassword())){
            map.put("worknum",user.getWorknum());
            map.put("department",user.getDptname());
            map.put("permission",user.getPermission());
            map.put("name",user.getName());
            map.put("token",token);
            redisUtils.set("login"+":"+worknum,map,60*60*24);
            return map;
        }
        return null;
    }



    /**
     * 获取类型
     * @return
     */
    public Map<String, Map<String, List<String>>> getTypeList(){
        List<Kind>list=mapper.getTypeList();

        Set<String> class1=new HashSet<> ();
        Set<String> class2=new HashSet<> ();
        Set<String> class3=new HashSet<> ();
        for (Kind kind:list) {
            String c1=kind.getClass1();
            String c2 = kind.getClass2();
            String c3 = kind.getClass3();
            if(c1!=null&&c2!=null&&c3!=null)
                class1.add(c1);
                class2.add(c2);
                class3.add(c3);
        }
        Map<String, Map < String, List<String> > > classMap1 =new HashMap<>();
        Map < String, List < String> >classMap2;
        List<String > list3;

        for (String str1: class1) {
            classMap2=new HashMap<>();
            for (String str2: class2) {
                list3=new ArrayList<>();
                for (Kind kind:list) {
                    if(str2.equals(kind.getClass2())&&str1.equals(kind.getClass1())){
                        list3.add(kind.getClass3());
                    }
                }
                if(!list3.isEmpty())
                    classMap2.put(str2, list3);

            }
            if (!classMap2.isEmpty()){
                classMap1.put(str1, classMap2);
            }

        }
        boolean res=redisUtils.set("class"+":"+"class", classMap1,60*60*24*7);

        if(res)
            return classMap1;
        else
            throw new MyException("缓存异常");
    }

    /**
     * 获取级别服务
     * @return
     */
    public Set<String> getLevelSet(){
        Set set=mapper.getLevelSet();
        boolean res;
        if(set==null&&set.isEmpty()){
            return null;
        }else {
            res=redisUtils.set("class:level",set,60*60*24*7);
        }
        if(res)
            return set;
        else

            throw new MyException("缓存异常");

    }

    /**
     * 获取部门
     * @return
     */
    public Map<String ,Integer> getDepartmentList(){
        Set<Department> set = mapper.getDepartmentList();
        Map<String ,Integer> map=new TreeMap<>();
        boolean res;
        if(set==null&&set.isEmpty()){
            return null;
        }else {
            for (Department  dpt:set) {
                map.put(dpt.getDptname(), (int) dpt.getId());
            }
            res=redisUtils.set("department", map, 60 * 60 * 24 * 7);
        }

        if(res){
            return map;
        }
        throw new MyException("缓存异常");
    }


    /**
     * 更新建设类的信息
     * @param construction
     * @return
     */
    public boolean updateConstructionServ(ConstructionInfo construction){
       int res= mapper.updateConstruction(construction);
       if(res>0){
           return true;
       }
       return  false;
    }

    /**
     * 更新成果类信息
     * @param info
     * @return
     */
    public boolean updateAchievementServ(AchievementInfo info){
        int res= mapper.updateAchievement(info);
        if(res>0){
            return true;
        }
        return  false;
    }

    /**
     * 更新获奖类
     * @param info
     * @return
     */
    public boolean updateAwardServ(AwardInfo info){
        int res=mapper.updateAward(info);
        if(res>0){
            return true;
        }
        return  false;
    }
}
