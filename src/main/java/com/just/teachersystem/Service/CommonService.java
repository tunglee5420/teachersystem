package com.just.teachersystem.Service;
import com.just.teachersystem.Entity.Department;
import com.just.teachersystem.VO.ConstructionInfo;
import com.just.teachersystem.VO.UserInfo;
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
    Map<String, Object> login(String worknum, String password);


    Map<String, Map<String, List<String>>> getTypeList();

    Set<String> getLevelSet();

    Map<String ,Integer>  getDepartmentList();

    boolean updateConstructionServ(ConstructionInfo construction);
}
