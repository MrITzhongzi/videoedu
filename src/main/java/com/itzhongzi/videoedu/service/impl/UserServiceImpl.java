package com.itzhongzi.videoedu.service.impl;

import com.itzhongzi.videoedu.config.WeChatConfig;
import com.itzhongzi.videoedu.domain.User;
import com.itzhongzi.videoedu.mapper.UserMapper;
import com.itzhongzi.videoedu.service.UserService;
import com.itzhongzi.videoedu.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User SaveWeChatUser(String code) {

        String accessTokenUrl = String.format(WeChatConfig.getOpenAccessTokenUrl(),weChatConfig.getOpenAppid(), weChatConfig.getOpenAppsecret(), code);
        //获取access_token
        Map<String, Object> map = HttpUtils.doGet(accessTokenUrl);
        if(map == null || map.isEmpty()) {
            return null;
        }
        String accessToken = (String) map.get("access_token");
        String openid = (String) map.get("openid");

        User dbUser = userMapper.findByopenid(openid);
        if(dbUser != null) {
            //数据库有用户， 更新用户直接返回
            return dbUser;
        }

        //获取用户基本信息
        String userInfoUrl = String.format(WeChatConfig.getOpenUserInfoUrl(), accessToken, openid);
        Map<String, Object> userBaseMap = HttpUtils.doGet(userInfoUrl);
        if(userBaseMap == null || userBaseMap.isEmpty()) {
            return null;
        }
        String nickname = (String) userBaseMap.get("nockname");

        Double sexTemp = (Double) userBaseMap.get("sex");
        int sex = sexTemp.intValue();
        String province = (String) userBaseMap.get("province");
        String city = (String) userBaseMap.get("city");
        String country = (String) userBaseMap.get("country");
        String headimgurl = (String) userBaseMap.get("headimgurl");
        StringBuilder sb = new StringBuilder(country).append(province).append(city);
        String finalAddress = sb.toString();
        try {
            //解决中文乱码
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            finalAddress = new String(finalAddress.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(city);
        user.setOpenid(openid);
        user.setSex(sex);
        user.setCreateTime(new Date());

        userMapper.save(user);

        return user;
    }
}
