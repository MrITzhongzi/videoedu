package com.itzhongzi.videoedu.service;

import com.itzhongzi.videoedu.domain.VideoOrder;
import com.itzhongzi.videoedu.dto.VideoOrderDto;

/**
 * 订单接口
 */
public interface VideoOrderService {

    /**
     * 下单接口
     * @param videoOrderDto
     * @return
     */
    String save(VideoOrderDto videoOrderDto) throws Exception;

}
