package com.itzhongzi.videoedu.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itzhongzi.videoedu.domain.JsonData;
import com.itzhongzi.videoedu.dto.VideoOrderDto;
import com.itzhongzi.videoedu.exception.ItzhongziException;
import com.itzhongzi.videoedu.service.VideoOrderService;
import com.itzhongzi.videoedu.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/user/api/v1/order")
public class OrderController {

    @Autowired
    private VideoOrderService videoOrderService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Logger dataLogger = LoggerFactory.getLogger("dataLogger");


    @RequestMapping("add")
    public void saveOrder(@RequestParam(value = "video_id", required = true) int videoId,
                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ip = IpUtils.getIpAddr(request);
        int userId = (int)request.getAttribute("user_id");

//        int userId = 1;  //临时写死
//        String ip = "114.115.250.129";     // 临时写死后期修改
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setIp(ip);

        // 真正的微信支付生成的codeurl ，需要 真实的商户号，密钥等
         String codeUrl = videoOrderService.save(videoOrderDto);

        if (codeUrl == null) {
            throw new NullPointerException();
        }

        try {
            //生成二维码
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //设置纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //设置编码类型
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //
            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 400, 400, hints);
            ServletOutputStream out = response.getOutputStream();
            //把png二维码 用流写到前端
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
