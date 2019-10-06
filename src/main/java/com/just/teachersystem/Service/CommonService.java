package com.just.teachersystem.Service;

import com.just.teachersystem.VO.AchievementInfo;
import com.just.teachersystem.VO.AwardInfo;
import com.just.teachersystem.VO.ConstructionInfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import	java.util.Map;
import java.util.Set;

/**
 * 常见通用服务层
 */
@Service
@Component
public interface CommonService {
    /**
     * 登陆服务
     * @param worknum
     * @param password
     * @return
     */
    Map<String, Object> login(String worknum, String password);


    /**
     * 获取类型
     * @return
     */
    Map<String, Map<String, List<String>>> getTypeList();
    /**
     * 获取级别服务
     * @return
     */
    Set<String> getLevelSet();
    /**
     * 获取部门
     * @return
     */
    Map<String ,Integer>  getDepartmentList();
    /**
     * 更新建设类的信息
     * @param construction
     * @return
     */
    boolean updateConstructionServ(ConstructionInfo construction);
    /**
     * 更新成果类信息
     * @param info
     * @return
     */
    boolean updateAchievementServ(AchievementInfo info);

    /**
     * 更新获奖类
     * @param info
     * @return
     */
    boolean updateAwardServ(AwardInfo info);
}
