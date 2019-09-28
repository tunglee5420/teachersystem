package com.just.teachersystem.Service.ServiceImp;


import com.just.teachersystem.Entity.Kind;
import com.just.teachersystem.Mapper.RootMapper;
import com.just.teachersystem.Service.RootService;
import com.just.teachersystem.Utill.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
/**
 * 超级管理员服务实现层
 */
@Service
@Component
public class RootServiceImp implements RootService {
    @Autowired
    RootMapper root;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 超管添加类别
     * @param kind
     * @return
     */
    public boolean addType(Kind kind){
//        System.out.println(kind.toString());
        boolean res=root.addType(kind);
        if(res){
            redisUtils.del("class:class");
        }
        return res;
    }
}
