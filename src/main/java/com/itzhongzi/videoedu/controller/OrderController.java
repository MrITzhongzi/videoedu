package com.itzhongzi.videoedu.controller;

import com.itzhongzi.videoedu.domain.JsonData;
import com.itzhongzi.videoedu.dto.VideoOrderDto;
import com.itzhongzi.videoedu.service.VideoOrderService;
import com.itzhongzi.videoedu.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private VideoOrderService videoOrderService;

    @RequestMapping("add")
    public JsonData saveOrder(@RequestParam(value = "video_id", required = true) int videoId,
                              HttpServletRequest request) throws Exception {
        String ip = IpUtils.getIpAddr(request);
//        int userId = request.getAttribute("user_id");
        int userId = 1;

        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setIp(ip);

        videoOrderService.save(videoOrderDto);

        return JsonData.buildSuccess("下单成功");
    }
}
