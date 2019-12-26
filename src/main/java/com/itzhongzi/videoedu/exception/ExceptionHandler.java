package com.itzhongzi.videoedu.exception;

import com.itzhongzi.videoedu.domain.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理控制器
 */
@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)  //捕获哪种类型的异常
    @ResponseBody  //响应给前端一个json格式
    public JsonData Handler(Exception e){
        if(e instanceof ItzhongziException) {
            //通过这种方式把异常告诉前端
            ItzhongziException itzhongziException = (ItzhongziException) e;
            return JsonData.buildError(itzhongziException.getMsg(), itzhongziException.getCode());
        } else {
            return JsonData.buildSuccess("全剧异常，未知错误");
        }
    }
}
