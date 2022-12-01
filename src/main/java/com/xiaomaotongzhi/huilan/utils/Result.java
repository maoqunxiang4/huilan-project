package com.xiaomaotongzhi.huilan.utils;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class Result {
    private Integer state ;
    private String message ;
    private Object data ;

    public static Result ok (Integer state )  {
        Result result = new Result();
        result.setState(state);
        return result ;
    }

    public static Result ok (Integer state , Object data) {
        Result result = new Result();
        result.setData(data);
        result.setState(state);
        return result ;
    }

    public static Result fail (Integer state , String message) {
        Result result = new Result();
        result.setState(state);
        result.setMessage(message);
        return result ;
    }
}
