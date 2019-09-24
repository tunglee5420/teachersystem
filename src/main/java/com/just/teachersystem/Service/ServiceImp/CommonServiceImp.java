package com.just.teachersystem.Service.ServiceImp;
import	java.util.HashMap;
import com.just.teachersystem.Mapper.CommonMapper;
import com.just.teachersystem.Service.CommonService;
import com.just.teachersystem.Utill.JwtUtils;
import com.just.teachersystem.VO.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 常见通用服务实现层
 */
@Service
public class CommonServiceImp  implements CommonService {
    @Autowired
    CommonMapper mapper;
    public Map<String, Object> login(String worknum, String password){
        if(worknum==null || password == null){
            return null;
        }
        UserInfo user=mapper.getInfo(worknum);
        String token=JwtUtils.creatJwt(user);

        Map<String, Object> map = new HashMap<> ();
        if(password.equals(user.getPassword())){
            map.put("worknum",user.getWorknum());
            map.put("department",user.getDptname());
            map.put("permission",user.getPermission());
            map.put("name",user.getName());
            map.put("token",token);
            return map;
        }
        return null;
    }

    /**
     * 更新用户信息服务层
     * @param userInfo
     * @return
     */
    public boolean updateUserInfo(UserInfo userInfo){
        int res= mapper.updateUserInfo(userInfo);
        return res==1?true:false;
    }
}
