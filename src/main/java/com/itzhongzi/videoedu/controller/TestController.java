package com.itzhongzi.videoedu.controller;

import com.itzhongzi.videoedu.config.WeChatConfig;
import com.itzhongzi.videoedu.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("test")
    public String test(){
        return "hello video 666 ";
    }

    @Autowired
    private WeChatConfig weChatConfig;

    @RequestMapping("test_config")
    public String testConfig(){
        System.out.println(weChatConfig.toString());
        return "hello config";
    }

    @Autowired
    private VideoMapper videoMapper;

    @RequestMapping("test_db")
    public Object testDb(){

        return videoMapper.findAll();
    }
}
