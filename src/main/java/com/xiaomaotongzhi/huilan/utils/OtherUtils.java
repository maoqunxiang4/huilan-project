package com.xiaomaotongzhi.huilan.utils;

import com.xiaomaotongzhi.huilan.utils.exception.ServiceException;
import javafx.concurrent.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;

public class OtherUtils {
    public static <E> Result showContent(List list , E obj)  {
        if (list==null) return Result.ok(200,obj) ;
        else return Result.ok(200 , list) ;
    }

    public static void NotBeNull(String... strings ){
        for (String string : strings) {
            if (string==""||string==null) throw new ServiceException("输入内容不能为空") ;
        }
    }

    public static <E,Sign> E SearchBySign(Class<E> type , Sign sign , Function<Sign,E> function){
        E e = function.apply(sign);
        if (e==null){
            throw new ServiceException("无法查找到指定数据");
        }
        return e ;
    }
}
