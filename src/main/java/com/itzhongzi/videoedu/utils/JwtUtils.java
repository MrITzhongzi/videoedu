package com.itzhongzi.videoedu.utils;

import com.itzhongzi.videoedu.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    /*
     * 发行者
     * */
    public static final String SUBJECT = "itzhongzi";
    /*
     * 过期时间为 一周
     * */
    public static final long EXPIRE = 1000 * 60 * 60 * 24 * 7;
    /*
     * 密钥
     * */
    public static final String APPSECRET = "it666";

    /**
     * 生成 JWT
     *
     * @param user
     * @return
     */
    public static String geneJsonWebToken(User user) {

        if (user == null || user.getId() == null || user.getName() == null || user.getHeadImg() == null) {
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("image", user.getHeadImg())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, APPSECRET)
                .compact();

        return token;
    }

    /**
     * 校验 token
     *
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception e) { }
        return null;
    }

}
