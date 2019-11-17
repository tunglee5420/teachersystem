package com.just.teachersystem.Mapper;

import com.just.teachersystem.Entity.UserLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserLogMapper {
    /**
     * 插入用户操作日志
     * @param log
     * @return
     */
    int insertUserLog(UserLog log);

    /**
     * 查询用户操作日志
     * @return
     */
     List<UserLog> getUserLogs();
}
