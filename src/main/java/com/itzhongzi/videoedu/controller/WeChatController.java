package com.itzhongzi.videoedu.controller;

import com.itzhongzi.videoedu.config.WeChatConfig;
import com.itzhongzi.videoedu.domain.JsonData;
import com.itzhongzi.videoedu.domain.User;
import com.itzhongzi.videoedu.domain.VideoOrder;
import com.itzhongzi.videoedu.service.UserService;
import com.itzhongzi.videoedu.service.VideoOrderService;
import com.itzhongzi.videoedu.utils.CommonUtils;
import com.itzhongzi.videoedu.utils.JwtUtils;
import com.itzhongzi.videoedu.utils.WXPayUtils;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@RestController
@RequestMapping("/api/v1/wechat")
public class WeChatController {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoOrderService videoOrderService;

    /**
     * 拼装微信扫一扫登录url
     * @param accessPage
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("login_url")
    public JsonData loginUrl(@RequestParam(value = "access_page", required = true) String accessPage) throws UnsupportedEncodingException {
        String redirectUrl = weChatConfig.getOpenRedirectUrl();
        String callbackUrl = URLEncoder.encode(redirectUrl, "GBK");
        String qrcodeUrl = String.format(WeChatConfig.getOpenQrcodeUrl(), weChatConfig.getOpenAppid(), redirectUrl, accessPage);
        return JsonData.buildSuccess(qrcodeUrl);
    }

    /**
     * 假数据测试地址
     * @param accessPage
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("login_url_test")
    public JsonData loginUrlTest(@RequestParam(value = "access_page", required = true) String accessPage) throws UnsupportedEncodingException {
        String name = "李洪伟";
        String headImg = "http://img.hapem.cn/DDK_03.png";
        User user = new User();
        user.setName(name);
        user.setHeadImg(headImg);
        user.setId(1);
        String token = JwtUtils.geneJsonWebToken(user);
        String backUrl = accessPage;
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("headImg", headImg);
        userMap.put("token", token);
        userMap.put("backUrl", backUrl);
        return JsonData.buildSuccess(userMap);
    }

    /**
     * 微信登陆扫码回调地址
     * @param code
     * @param state
     * @param response
     */
    @GetMapping("user/callback")
    public void wechatUserCallback(@RequestParam(value = "code", required = true) String code,
                                   String state, HttpServletResponse response) throws IOException {
        System.out.println("code: " + code);
        System.out.println("state: " + state);

        User user = userService.SaveWeChatUser(code);
        if(user != null) {
            //生成jwt 验证码
            String token = JwtUtils.geneJsonWebToken(user);
            // state 当前用户的页面需要拼接 http:// 这样才不会站内跳转
            response.sendRedirect(state + "?token=" + token + "&head_img=" + user.getHeadImg() + "&name=" + URLEncoder.encode(user.getName(), "UTF-8"));

        }
    }

    /**
     * 测试 回调接口
     * @param code
     * @param state
     * @param response
     * @throws IOException
     */
    @GetMapping("user/callback_test")
    public void wechatUserCallbackTest(@RequestParam(value = "code", required = true) String code,
                                   String state, HttpServletResponse response) throws IOException {

        User user = userService.SaveWeChatUserTest();
        if(user != null) {
            //生成jwt 验证码
            String token = JwtUtils.geneJsonWebToken(user);
            System.out.println("token:" + token);
            // state 当前用户的页面需要拼接 http:// 这样才不会站内跳转
            String url = state + "?token=" + token + "&head_img=" + user.getHeadImg() + "&name=" + URLEncoder.encode(user.getName(), "UTF-8");
            response.sendRedirect(url);

        }
    }

    /**
     * 微信支付回调
     *
     */
    @RequestMapping("order/callback")
    public void orderCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //微信返回的数据都是以流的方式返回
        InputStream inputStream = request.getInputStream();
        // bufferreader是包装设计模式，性能更高
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        inputStream.close();
        Map<String, String> callbackmap = WXPayUtils.xmlToMap(sb.toString());

        SortedMap<String, String> sortedmap = WXPayUtils.getSortedmap(callbackmap);
        //判断签名是否正确
        if(WXPayUtils.isCorrectSign(sortedmap, weChatConfig.getKey())){
            //
            if("SUCCESS".equals(sortedmap.get("result_code"))){
                String outTradeNo = sortedmap.get("out_trade_no");
                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);
                if(dbVideoOrder != null && dbVideoOrder.getState() == 0) { //判断逻辑业务场景
                    VideoOrder videoOrder = new VideoOrder();
                    videoOrder.setOpenid(sortedmap.get("openid"));
                    videoOrder.setOutTradeNo(outTradeNo);
                    videoOrder.setNotifyTime(new Date());
                    videoOrder.setState(1);

                    int rows = videoOrderService.updateVideoOrderByOutTradeNo(videoOrder);
                    if(rows == 1) { //需要通知微信，订单处理成功
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }

            }
        }

        //都处理失败 (通知微信，订单处理失败)
        response.setContentType("text/xml");
        response.getWriter().println("fail");

    }

}
