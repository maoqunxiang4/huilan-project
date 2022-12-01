package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.utils.Result;

import java.time.LocalDateTime;

public interface IActivityService {
    Result addActivity(String title , String content , String time ) ;
    Result updateActivity(Integer id , String title , String content , String time ) ;
    Result deleteActivity(Integer id) ;
    Result showActivity() ;
}
