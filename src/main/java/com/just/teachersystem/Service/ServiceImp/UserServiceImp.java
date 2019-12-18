package com.just.teachersystem.Service.ServiceImp;

import com.just.teachersystem.Mapper.*;
import com.just.teachersystem.Service.UserService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.Utill.YearUtils;
import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

/**
 * 普通用户服务实现层
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImp implements UserService {

    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ConstructionMapper construction;

    @Autowired
    AchievementMapper achievement;

    @Autowired
    AwardMapper award;

    /**
     * 验证身份
     * @param worknum
     * @param password
     * @return
     */
    public boolean check(String worknum,String password){
        UserInfo userInfo=teacherMapper.getInfo(worknum);
        if(userInfo == null) return false;
        return EncryptUtil.getInstance().MD5(password).equals(userInfo.getPassword());
    }

    /**
     * 根据工号查个人信息
     * @param worknum
     * @return
     */
    public UserInfo getUserInfo(String worknum){
        if(worknum==null) return null;
        UserInfo user=teacherMapper.getInfo(worknum);
        return user;
    }

    /**
     * 获取个人建设类信息
     * @param info
     * @return
     */
    public List getMyConstructions(ConstructionInfo info){
        if (info==null) return null;
        List list = construction.selectConstructions(info);
        return list;
    }

    /**
     * 获取个人成果类信息
     * @param info
     * @return
     */
    public List getMyAchievements(AchievementInfo info){
        if (info==null) return null;
        List list=achievement.selectAchievements(info);
        return list;
    }

    /**
     *获取个人获奖类信息
     * @param info
     * @return
     */
    public List getMyAwards(AwardInfo info){
        if (info==null) return null;
        List list=award.selectAwards(info);
        return list;
    }
    /**
     * 添加建设类信息
     * @param info
     * @return
     */
    public int addConstruction(ConstructionInfo info){
        info.setStatus(0);
        info.setClass1("建设类");
        info.setSchoolYear(YearUtils.getSchoolYear(info.getStartTime()));
        info.setIsEnd(0);
        info.setYear(YearUtils.getYears(info.getStartTime()));

        return construction.insertToConstruction(info);
    }

    /**
     * 获得用户个人建设类记录
     * @param worknum
     * @return
     */
    public List getMyConstructionInfo(String worknum){
        List list=construction.selectConstructionByWorknum(worknum);
        if(list == null||list.size()==0){
            return null;
        }
        return list;
    }

    /**
     * 添加成果类信息
     * @param info
     * @return
     */
    public boolean addAchievement(AchievementInfo info){

        String time=String.valueOf(info.getPublishTime());
        info.setStatus(0);
        info.setClass1("成果类");
        info.setSchoolYear(YearUtils.getSchoolYear(time));
        info.setYear(YearUtils.getYears(time));
        System.out.println(info);
        int res=achievement.insertToAchievement(info);
        if(res ==0){
            return false;
        }
        return true;
    }


    /**
     * 获得用户个人成果类记录
     * @param worknum
     * @return
     */
    public List getMyAchievementInfo(String worknum){
        List list = achievement.selectAchievementByWorknum(worknum);
        if(list == null||list.size()==0){
            return null;
        }
        return list;

    }

    /**
     * 添加获奖类信息
     * @param info
     * @return
     */
    public boolean addAward(AwardInfo info){
        String time = String.valueOf(info.getAwardTime());
        info.setStatus(0);
        info.setClass1("获奖类");
        info.setAwardYear(YearUtils.getYears(time));
        info.setSchoolYear(YearUtils.getSchoolYear(time));
        info.setYear(YearUtils.getYears(time));
        System.out.println(info);
        int res = award.insertToAward(info);
        if(res ==0){
            return false;
        }
        return true;

    }

    /**
     * 获得用户个人获奖类记录
     * @param worknum
     * @return
     */
    public List getMyAwardInfo(String worknum){
        List list = award.selectAwardByWorknum(worknum);
        if(list == null||list.size()==0){
            return null;
        }
        return list;
    }
}
