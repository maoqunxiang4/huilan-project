package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.utils.Result;
import io.swagger.models.auth.In;

import java.time.LocalDateTime;

public interface IActivityContentService {
    Result updateActivityContent(Integer id , String title , String content) ;
    Result showActivityContent(Integer id ) ;
}
