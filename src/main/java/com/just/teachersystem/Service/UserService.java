package com.just.teachersystem.Service;

import com.just.teachersystem.VO.ConstructionInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 普通用户服务层
 */
@Service
public interface UserService {
    boolean check(String worknum,String password);
    int addConstruction(ConstructionInfo info);


    List getMyConstructionInfo(String worknum);
}
