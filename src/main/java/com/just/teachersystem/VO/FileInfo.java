package com.just.teachersystem.VO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileInfo {
    private String filename;
    private String expireTime;
    private boolean  privacy;
    private long size;
    private String dirPath;

}
