package com.itzhongzi.videoedu.controller;

import com.itzhongzi.videoedu.domain.JsonData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/user/api/v1/order")
public class OrderController {

    @RequestMapping("add")
    public JsonData saveOrder(){
        return JsonData.buildSuccess("下单成功");
    }
}
