package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.utils.Result;
import io.swagger.models.auth.In;

public interface IPlaceService {
    Result addPlace(Integer gid , String placename , String time) ;
    Result updatePlace(Integer pid, Integer gid ,String placename , String time ) ;
    Result deletePlace(Integer id) ;
    Result showPlace(Integer gid) ;
    Result searchPlace(Integer gid , String start , String end ) ;
}
