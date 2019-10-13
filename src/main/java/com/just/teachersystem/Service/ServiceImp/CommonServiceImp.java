package com.just.teachersystem.Service.ServiceImp;
import	java.awt.Label;
import java.util.*;

import com.just.teachersystem.Entity.Department;
import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.Entity.Level;
import com.just.teachersystem.Mapper.*;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.Exception.MyException;
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
    @Autowired
    ConstructionMapper constructionMapper;

    @Autowired
    AchievementMapper achievementMapper;
    @Autowired
    AwardMapper awardMapper;
    @Autowired
    TeacherMapper teacher;


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
        UserInfo user=teacher.getInfo(worknum);
        if(user==null) return null;
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
    public List getTypeList(String cl1){
        List<Kind>list=mapper.getTypeList();

        int t,k;
        t=k=0;


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

//        System.out.println(cl1) ;
        if(!class1.contains(cl1)){
            return null;
        }
        List<Object>l=new ArrayList<> ();
        Map < String, Object >classMap2=null;
        List<Map > list3;
        Map<String ,Object> dmap=null;
        for (String str2: class2) {
            classMap2=new HashMap<>();
            list3=new ArrayList<>();
            for (Kind kind:list) {
                if(str2.equals(kind.getClass2())&&cl1.equals(kind.getClass1())){
                    dmap=new HashMap<> ();
                    dmap.put("label",kind.getClass3()) ;
                    dmap.put("value",++k);
                    list3.add(dmap);
                }
            }
            k=0;

            if(!list3.isEmpty()){
                classMap2.put("label",str2);
                classMap2.put("value", ++t);
                classMap2.put("children", list3);

            }
            if(!classMap2.isEmpty()){
                l.add(classMap2);
            }


        }
        System.out.println(l);
        boolean res=  redisUtils.set("class:"+cl1, l,60*60*24*7);

        if(res)
            return l;
        else
            throw new MyException("缓存异常");
    }

    /**
     * 获取级别服务
     * @return
     */
    public Set getLevelSet(){
        Set<String> set=mapper.getLevelSet();

        Set<Map < String, Object>>sets=new HashSet<> ();
        boolean res;
        int k=1;
        if(set==null&&set.isEmpty()){
            return null;
        }else {
            for (String s : set) {
                Map<String, Object> map = new HashMap<> ();
                map.put("label",s);
                map.put("value", k++);
                sets.add(map);
            }

            res=redisUtils.set("class:level",sets,60*60*24*7);
        }
        if(res)
            return sets;
        else

            throw new MyException("缓存异常");

    }

    /**
     * 获取部门
     * @return
     */
    public List getDepartmentList(){
        Set<Department> set = mapper.getDepartmentList();
        List<Map> list = new LinkedList<> ();
        Map<String, Object> map1 ;
        boolean res;
        if(set==null&&set.isEmpty()){
            return null;
        }else {
            for (Department e:set) {
                map1 = new TreeMap<> ();
                map1.put("label",e.getDptname());
                map1.put("value", e.getId());

                list.add(map1);
            }
            res=redisUtils.set("department", list, 60 * 60 * 24 * 7);
        }

        if(res){
            return list;
        }
        throw new MyException("缓存异常");
    }


    /**
     * 更新建设类的信息
     * @param construction
     * @return
     */
    public boolean updateConstructionServ(ConstructionInfo construction){
       int res= constructionMapper.updateConstruction(construction);
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
        int res= achievementMapper.updateAchievement(info);
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
        int res=awardMapper.updateAward(info);
        if(res>0){
            return true;
        }
        return  false;
    }
}
