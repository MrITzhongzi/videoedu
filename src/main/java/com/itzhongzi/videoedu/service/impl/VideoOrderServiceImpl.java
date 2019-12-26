package com.itzhongzi.videoedu.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itzhongzi.videoedu.config.WeChatConfig;
import com.itzhongzi.videoedu.domain.User;
import com.itzhongzi.videoedu.domain.Video;
import com.itzhongzi.videoedu.domain.VideoOrder;
import com.itzhongzi.videoedu.dto.VideoOrderDto;
import com.itzhongzi.videoedu.mapper.UserMapper;
import com.itzhongzi.videoedu.mapper.VideoMapper;
import com.itzhongzi.videoedu.mapper.VideoOrderMapper;
import com.itzhongzi.videoedu.service.VideoOrderService;
import com.itzhongzi.videoedu.utils.CommonUtils;
import com.itzhongzi.videoedu.utils.HttpUtils;
import com.itzhongzi.videoedu.utils.WXPayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatConfig weChatConfig;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Logger dataLogger = LoggerFactory.getLogger("dataLogger");


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String save(VideoOrderDto videoOrderDto) throws Exception {

        dataLogger.info("mudule=video_order`api=save`user_id={}`video_id={}", videoOrderDto.getUserId(), videoOrderDto.getVideoId());

        //查找视频信息
        Video video = videoMapper.findById(videoOrderDto.getVideoId());
        //查找用户信息
        User user = userMapper.findByid(videoOrderDto.getUserId());


        //生成订单
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setVideoId(video.getId());
        videoOrder.setState(0);
        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg(user.getHeadImg());
        videoOrder.setNickname(user.getName());

        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrder.setOutTradeNo(CommonUtils.generateUUID());

        videoOrderMapper.insert(videoOrder);

        //获取codeurl 真正的微信支付生成的codeurl ，需要 真实的商户号，密钥等, 暂时写死
        // String nifiedOrde = unifiedOrder(videoOrder);

        String nifiedOrde = new GsonBuilder().create().toJson(videoOrder);


        return nifiedOrde;
    }


    /**
     * 统一下单方法
     *
     * @return
     */
    private String unifiedOrder(VideoOrder videoOrder) throws Exception {

        //生成签名
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", weChatConfig.getAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.generateUUID());
        params.put("body", videoOrder.getVideoTitle());
        params.put("out_trade_no", videoOrder.getOutTradeNo());
        params.put("total_fee", videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip", videoOrder.getIp());
        params.put("notify_url", weChatConfig.getPayCallbackUrl());
        params.put("trade_type", "NATIVE");

        //sign签名
        String sign = WXPayUtils.createSign(params, weChatConfig.getKey());
        params.put("sign", sign);

        //map转xml
        String payXml = WXPayUtils.mapToXml(params);

        System.out.println(payXml);
        //统一下单
        String orderStr = HttpUtils.doPost(WeChatConfig.getUnifiedOrderUrl(), payXml, 4000);
        if (null == orderStr) {
            return null;
        }
        Map<String, String> unifiedOrderMap = WXPayUtils.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());
        if (unifiedOrderMap != null) {
            return unifiedOrderMap.get("code_url");
        }
        return null;
    }

    @Override
    public VideoOrder findByOutTradeNo(String outTradeNo) {

        return videoOrderMapper.findByOutTradeNo(outTradeNo);
    }

    @Override
    public int updateVideoOrderByOutTradeNo(VideoOrder videoOrder) {
        return videoOrderMapper.updateVideoOrderByOutTradeNo(videoOrder);
    }

}
