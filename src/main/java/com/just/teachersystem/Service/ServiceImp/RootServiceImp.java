package com.just.teachersystem.Service.ServiceImp;

import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.Mapper.BonusMapper;
import com.just.teachersystem.Mapper.CommonMapper;
import com.just.teachersystem.Mapper.PerformanceMapper;
import com.just.teachersystem.Mapper.TeacherMapper;
import com.just.teachersystem.Service.RootService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.Utill.RedisUtils;
import com.just.teachersystem.VO.BonusInfo;
import com.just.teachersystem.VO.PerformanceInfo;
import com.just.teachersystem.VO.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 超级管理员服务实现层
 */
@Service
@Component
public class RootServiceImp implements RootService {

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TeacherMapper teacher;

    @Autowired
    CommonMapper common;
    @Autowired
    PerformanceMapper performance;
    @Autowired
    BonusMapper bonus;

    /**
     * 超管添加类别
     * @param kind
     * @return
     */
    public boolean addType(Kind kind){
        if(kind==null ||kind.getClass1()== null||kind.getClass1().equals("")||kind.getClass2()==null||kind.getClass2().equals(""))
            return false;
        boolean res=common.addType(kind);
        if(res){
            redisUtils.del("class:class");
        }
        return res;
    }

    /**
     * 超管删除类别(仅限class3)
     * @param class3
     * @return
     */
    public boolean deleteType(String class3) {
        if(class3.equals("")||class3==null)
            return false;
        boolean res = common.deleteType(class3);
        if(res)
            redisUtils.del("class:class");
        return res;
    }


    /**
     * 添加级别
     * @param level
     * @return
     */
    public boolean addLevel(String level){
        if(level==null || level.equals(""))
            return false;
        boolean res=common.addLevel(level);
        if(res){
            redisUtils.del("class:level");
        }
        return res;
    }

    /**
     * 删除级别
     * @param level
     * @return
     */
    public boolean deleteLevel(String level){
        if(level==null || level.equals(""))
            return false;
        boolean res = common.deleteLevel(level);
        if(res){
            redisUtils.del("class:level");
        }
        return res;
    }

    /**
     *添加新的用户成员
     * @param user
     * @return
     */
    public boolean addUser(UserInfo user){
        user.setPassword(EncryptUtil.getInstance().MD5(user.getPassword()));
        if(user==null)
            return false;
        int res = teacher.insertUser(user);
        return res<0?false:true;
    }

    /**
     * 更新用户信息(包括修改用户权限和密码)
     * @param userInfo
     * @return
     */
    public boolean updateUserInfo(UserInfo userInfo){
        if (userInfo==null) return false;
        if(userInfo.getPassword()!=null )  userInfo.setPassword(EncryptUtil.getInstance().MD5(userInfo.getPassword()));
        int res= teacher.updateUserInfo(userInfo);
        return res==1?true:false;
    }

    /**
     * 根据工号删除用户
     * @param worknum
     * @return
     */
    public boolean deleteUser(String worknum){
        if(worknum.equals("")||worknum==null ){
            return false;
        }
        int res=teacher.deleteByWorknum(worknum);
        return res==1?true:false;
    }

    /**
     * 检索用户信息
     * @param info
     * @return
     */
    public List<UserInfo> getUserInfo(UserInfo info){
        if(info==null) return null;
        List<UserInfo> users = teacher.getUserInfoList(info);
        return users;
    }

    /**
     * 条件检索业绩类
     * @param info
     * @return
     */
    public List<PerformanceInfo> getPerfromanceList(PerformanceInfo info){
        if(info==null){
            return null;
        }
        info.setStatus(1);
        List < PerformanceInfo>list=performance.selectPerformanceList(info);
        if(list==null) return null;
        return list;
    }

    /**
     * 更新(删除 status置0)业绩分信息表
     * @param info
     * @return
     */
    public boolean updatePerformanceInfo(PerformanceInfo info){
        if(info == null) return false;
        int res=performance.updatePerformance(info);
        return res ==0?false:true;
    }

    /**
     * 添加业绩分信息表
     * @param info
     * @return
     */
    public boolean addPerformanceInfo(PerformanceInfo info){
        if(info == null) return false;
        int res=performance.insertToPerformance(info);
        return res ==0?false:true;
    }

    /**
     * 根据条件检索奖金信息表
     * @param info
     * @return
     */
    public List<BonusInfo> getBonusList(BonusInfo info){
        if(info==null){
            return null;
        }
        info.setStatus(1);
        List < BonusInfo>list=bonus.selectBonusList(info);
        if(list==null) return null;
        return list;
    }

    /**
     * 更新(删除 status置0)奖金信息表
     * @param info
     * @return
     */
    public boolean updateBonusInfo(BonusInfo info){
        if(info == null) return false;
        int res=bonus.updateBonus(info);
        return res ==0?false:true;
    }

    /**
     * 添加奖金信息表
     * @param info
     * @return
     */
    public boolean addBonusInfo(BonusInfo info){
        if(info == null) return false;
        int res=bonus.insertToBonus(info);
        return res ==0?false:true;
    }


}
