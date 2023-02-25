package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.utils.Result;

public interface IPlaceAppointmentService {
    public Result addPlaceAppoinment(Integer pid) ;
    public Result deletePlaceAppoinment(Integer pid) ;
    public Result showPlaceAppoinment(Integer current) ;
}
