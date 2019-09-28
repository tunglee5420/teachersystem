package com.just.teachersystem.Service;

import com.just.teachersystem.Entity.Kind;
import org.springframework.stereotype.Service;

/**
 * 超级管理员服务层
 */
@Service
public interface RootService {
    boolean addType(Kind kind);
}
