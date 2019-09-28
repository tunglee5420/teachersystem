package com.just.teachersystem.Service.ServiceImp;

import com.just.teachersystem.Mapper.CommonMapper;
import com.just.teachersystem.Service.UserService;
import com.just.teachersystem.Utill.EncryptUtil;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 普通用户服务实现层
 */
@Service
public class UserServiceImp implements UserService {
    @Autowired
    CommonMapper commonMapper;

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
}
