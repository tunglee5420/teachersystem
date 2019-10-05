package com.just.teachersystem.Service;

import com.just.teachersystem.Utill.HttpClientResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



@Component
@Service
public interface FileService {
    HttpClientResult filePanLogin();
}
