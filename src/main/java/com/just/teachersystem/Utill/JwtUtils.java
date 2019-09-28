package com.just.teachersystem.Utill;

import com.just.teachersystem.Entity.User;
import com.just.teachersystem.VO.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt工具类
 */
@Component
public class JwtUtils {
    public static final String SUBJECT="justTeacherSystem";
    public static final long Ex_Time=60*1000*60*24;//过期时间，毫秒
    public static final String APPSECRET="JUST";//密钥

    /**
     * 加密token
     * @param user
     * @return
     */
    public static String creatJwt(UserInfo user){

        if(user==null){
            return "";
        }
        String token= Jwts.builder().setSubject(SUBJECT)
                .claim("permission",user.getPermission())
                .claim("worknum",user.getWorknum())
                .claim("department", user.getDptname())
                .setIssuedAt(new Date())
                .setNotBefore(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Ex_Time))
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();
        return token;
    }

    /**
     * 解密token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token){
        try {
            final Claims claims= Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        }catch (Exception e){
            return null;
        }

    }
}
