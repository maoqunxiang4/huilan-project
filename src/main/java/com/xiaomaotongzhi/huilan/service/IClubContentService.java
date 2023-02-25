package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.utils.Result;
import io.swagger.models.auth.In;
import org.springframework.data.redis.core.script.ReactiveScriptExecutor;

public interface IClubContentService {
    Result addClubContent(String title ,String content) ;
    Result deleteClubContent(Integer id) ;
    Result showAllClubContent(Integer current) ;
    Result showSpecificClubContent(Integer id) ;
    Result updateClubContent(Integer id ,String title ,String content) ;
    Result changeLike(Integer id) ;
    Result isLiked(Integer id) ;
}
