package com.just.teachersystem.Service.ServiceImp;
import	java.util.ArrayList;

import com.just.teachersystem.Entity.User;
import com.just.teachersystem.Mapper.*;
import com.just.teachersystem.Service.CollegeAdminService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.VO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学院管理员服务实现层
 */
@Service
public class CollegeAdminServiceImp implements CollegeAdminService {

    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    ConstructionMapper construction;
    @Autowired
    AchievementMapper achievement;
    @Autowired
    AwardMapper award;
    @Autowired
    PerformanceMapper performance;
    @Autowired
    BonusMapper bonus;



    /**
     * 查询用户信息
     * @param info
     * @return
     */
    public List getUserInfo(UserInfo info){
        List list=teacherMapper.getUserInfoList(info);
        if(list == null||list.size()==0) return null;
        return list;
    }

    /**
     * 修改用户密码
     * @param worknum
     * @param password
     * @return
     */
    public int updateUserPassword(String worknum,String password,String department){
        if(worknum==null || password == null || department == null){
            return 0;
        }
        UserInfo userInfo=new UserInfo();
        userInfo.setWorknum(worknum);
        userInfo.setPassword(EncryptUtil.getInstance().MD5(password));
        UserInfo u= new UserInfo();
        String dptname=u.getDptname();
        int permissions = u.getPermission();
        if(dptname.equals(department)&&permissions<3) return teacherMapper.updateUserInfo(userInfo);
        return 0;
    }

    /**
     * 修改用户电话
     * @param worknum
     * @param phone
     * @return
     */
    public int uodateUserPhone(String worknum,String phone,String department){
        if(worknum == null || phone == null || department == null) return 0;
        UserInfo userInfo=new UserInfo();
        userInfo.setWorknum(worknum);
        userInfo.setPhone(phone);
        String dptname=teacherMapper.getInfo(worknum).getDptname();
        if(dptname.equals(department))
            return teacherMapper.updateUserInfo(userInfo);
        return 0;
    }

    /**
     * 获取部门建设类信息
     * @param info
     * @return
     */
    public List getDptConstructions(ConstructionInfo info){
        if (info==null) return null;
        List list = construction.selectConstructions(info);
        return list;
    }

    /**
     * 获取部门成果类信息
     * @param info
     * @return
     */
    public List getDptAchievements(AchievementInfo info){
        if (info==null) return null;
        List list=achievement.selectAchievements(info);
        return list;
    }

    /**
     *获取部门获奖类信息
     * @param info
     * @return
     */
    public List getDptAwards(AwardInfo info){
        if (info==null) return null;
        List list=award.selectAwards(info);
        return list;
    }


    /**
     * 学院管理员核审建设类
     * @param info
     * @return
     */
    public int  ConstructionCheck(ConstructionInfo info){
        if(info==null) return 0;
        int res= construction.updateConstruction(info);
        if(res>0) return 1;
        return 0;
    }

    /**
     * 获取确认业绩名单
     * @param info
     * @return
     */
    public List confirmPerformance(PerformanceInfo info){
        if(info == null)
            return null;
        List list=performance.selectPerformanceList(info);
        if (list==null){
            return null;
        }
        return list;
    }

    /**
     * 获取确认津贴名单
     * @param info
     * @return
     */
    public List<BonusInfo> confirmBonus(BonusInfo info){
        if(info == null)
            return null;
        List list=bonus.selectBonusList(info);
        if (list==null){
            return null;
        }
        return list;
    }

}
