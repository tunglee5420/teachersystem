package com.just.teachersystem.Service;

import com.just.teachersystem.Utill.HttpClientResult;
import com.just.teachersystem.VO.FileInfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;


@Component
@Service
public interface FileService {
    HttpClientResult filePanLogin();

    HttpClientResult getTgetToken(String cookie, FileInfo fileInfo, String worknum);

    HttpClientResult confirmed( String cookie,  String uuid);

    HttpClientResult getDownloadToken(String cookie, String uuid);
}
