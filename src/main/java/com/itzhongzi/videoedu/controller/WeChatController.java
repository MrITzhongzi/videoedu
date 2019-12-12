package com.itzhongzi.videoedu.controller;

import com.itzhongzi.videoedu.config.WeChatConfig;
import com.itzhongzi.videoedu.domain.JsonData;
import com.itzhongzi.videoedu.domain.User;
import com.itzhongzi.videoedu.service.UserService;
import com.itzhongzi.videoedu.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/v1/wechat")
public class WeChatController {

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserService userService;

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


}
