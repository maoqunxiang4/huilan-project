package com.xiaomaotongzhi.huilan.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.resource.StringResource;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisUtils {
    public static String filedValueToString(Object filedValue){
        if (filedValue==null){
            String value = (String) filedValue;
            value = "null" ;
            return value;
        }
        else return filedValue.toString() ;
    }

    public static <E> void RedisHashCache(StringRedisTemplate stringRedisTemplate
            , E obj ,String prefix , String sign , Integer expireTime , TimeUnit timeUnit){
        Map<String, Object> map = BeanUtil.beanToMap(obj,
                new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((filedName, filedValue) -> filedValueToString(filedValue)));
        if (sign!=null) {
            stringRedisTemplate.opsForHash().putAll(prefix + sign, map);
            stringRedisTemplate.expire(prefix + sign, expireTime, timeUnit);
        }else {
            stringRedisTemplate.opsForHash().putAll(prefix, map);
            stringRedisTemplate.expire(prefix , expireTime, timeUnit);
        }
    }

    public static void deleteCache(StringRedisTemplate stringRedisTemplate , String prefix , String sign){
        stringRedisTemplate.delete(prefix+sign) ;
    }

    public static <E> void RedisStringCache(StringRedisTemplate stringRedisTemplate
            , E obj ,String prefix , String sign , Integer expireTime , TimeUnit timeUnit){
        String jsonStr = JSONUtil.toJsonStr(obj);
        if (sign!=null) {
            stringRedisTemplate.opsForValue().set(prefix + sign, jsonStr, expireTime, timeUnit);
        }else{
            stringRedisTemplate.opsForValue().set(prefix, jsonStr, expireTime, timeUnit);
        }
    }
}
