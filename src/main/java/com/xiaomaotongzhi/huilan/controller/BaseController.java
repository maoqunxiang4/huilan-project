package com.xiaomaotongzhi.huilan.controller;

import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.exception.ServiceException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class BaseController {
//    @ExceptionHandler(ServiceException.class)
//    @ResponseBody
//    public Result ServiceException(Exception e){
//        return Result.fail(503,e.getMessage()) ;
//    }
}
