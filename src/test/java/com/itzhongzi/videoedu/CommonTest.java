package com.itzhongzi.videoedu;

import com.itzhongzi.videoedu.domain.User;
import com.itzhongzi.videoedu.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

public class CommonTest {

    @Test
    public void testJWT(){
        User user = new User();
        user.setId(999);
        user.setHeadImg("www.itzhongzi.com");
        user.setName("it");


        String token = JwtUtils.geneJsonWebToken(user);
        System.out.println("token: " + token);


    }

    @Test
    public void testCheck(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdHpob25nemkiLCJpZCI6OTk5LCJuYW1lIjoiaXQiLCJpbWFnZSI6Ind3dy5pdHpob25nemkuY29tIiwiaWF0IjoxNTc2MDU1MjM0LCJleHAiOjE1NzY2NjAwMzR9.VH8f6579SJk74HaPGfzgp5tyKVgOwjmu8E96QoR3OVs";
        Claims claims = JwtUtils.checkJWT(token);
        if(claims != null) {
            System.out.println(claims);
            String name = (String) claims.get("name");
            String img = (String) claims.get("image");
            int id = (Integer) claims.get("id");
            System.out.println(name);
            System.out.println(img);
            System.out.println(id);
        } else {
            System.out.println("解密失败，非法token");
        }

    }


}
