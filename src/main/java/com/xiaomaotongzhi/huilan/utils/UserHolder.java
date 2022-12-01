package com.xiaomaotongzhi.huilan.utils;

import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;

public class UserHolder {
    private static final ThreadLocal<UserVo> threadLocal = new ThreadLocal<>() ;

    public static UserVo getUser(){
        UserVo user = threadLocal.get();
        return user ;
    }

    public static void setUser(UserVo user) {
        threadLocal.set(user);
    }

    public static void removeUser(){
        threadLocal.remove();
    }
}
