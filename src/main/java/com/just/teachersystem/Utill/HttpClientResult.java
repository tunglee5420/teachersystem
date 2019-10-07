package com.just.teachersystem.Utill;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@ToString
public class HttpClientResult implements Serializable {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private Object content;

    public HttpClientResult(int statusCode, Object content) {
        this.code = statusCode;
        this.content = content;
    }

    public HttpClientResult(int scInternalServerError) {
        this.code = scInternalServerError;
    }
}
