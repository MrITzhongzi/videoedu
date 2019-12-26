package com.itzhongzi.videoedu.controller;

import com.itzhongzi.videoedu.config.WeChatConfig;
import com.itzhongzi.videoedu.exception.ItzhongziException;
import com.itzhongzi.videoedu.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    public Object testDb() {
        throw new ItzhongziException(-2, "自定义错误");
//        return videoMapper.findAll();
    }
}


/**
 * 测试接口 测试地址示例： http://localhost:8080/login_url_test?access_page=http://www.baidu.com
 * 模拟微信回调接口
 */
@Controller
class TestController2 {
    @Autowired
    private WeChatConfig weChatConfig;

    /**
     * 测试接口
     * @param accessPage
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("login_url_test")
    public String loginUrlTest(@RequestParam(value = "access_page", required = true) String accessPage) throws UnsupportedEncodingException {

        System.out.println("accessPage: " + accessPage);
        //转发请求，模拟微信回调接口
        return "forward:/api/v1/wechat/user/callback_test?code=code222&state=" + accessPage;
    }
}
