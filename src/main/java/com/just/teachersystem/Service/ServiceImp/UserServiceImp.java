package com.just.teachersystem.Service.ServiceImp;

import com.just.teachersystem.Mapper.CommonMapper;
import com.just.teachersystem.Mapper.RootMapper;
import com.just.teachersystem.Mapper.TeacherMapper;
import com.just.teachersystem.Service.UserService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.Utill.YearUtils;
import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

/**
 * 普通用户服务实现层
 */
@Service
public class UserServiceImp implements UserService {
    @Autowired
    CommonMapper commonMapper;

    @Autowired
    TeacherMapper teacherMapper;

    /**
     * 验证身份
     * @param worknum
     * @param password
     * @return
     */
    public boolean check(String worknum,String password){
        UserInfo userInfo=commonMapper.getInfo(worknum);
        if(userInfo == null) return false;
        return EncryptUtil.getInstance().MD5(password).equals(userInfo.getPassword());
    }

    /**
     * 添加建设类信息
     * @param info
     * @return
     */
    public int addConstruction(ConstructionInfo info){
        info.setStatus(0);
        info.setClass1("建设类");
        info.setSchoolyear(YearUtils.getSchoolYear());
        info.setIsEnd(0);
        info.setYear(YearUtils.getYears());

        return teacherMapper.insertToConstruction(info);
    }

    /**
     * 获得用户个人建设类记录
     * @param worknum
     * @return
     */
    public List getMyConstructionInfo(String worknum){
        List list=teacherMapper.selectConstructionByWorknum(worknum);
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
        info.setStatus(0);
        info.setClass1("成果类");
        info.setSchoolYear(YearUtils.getSchoolYear());
        info.setYear(YearUtils.getYears());
        System.out.println(info);
        int res=teacherMapper.insertToAchievement(info);
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
        List list = teacherMapper.selectAchievementByWorknum(worknum);
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
        info.setStatus(0);
        info.setClass1("获奖类");
        info.setAwardYear(info.getAwardTime().toString().substring(0,4));
        info.setSchoolYear(YearUtils.getSchoolYear());
        info.setYear(YearUtils.getYears());
        System.out.println(info);
        int res = teacherMapper.insertToAward(info);
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
        List list = teacherMapper.selectAwardByWorknum(worknum);
        if(list == null||list.size()==0){
            return null;
        }
        return list;
    }
}
