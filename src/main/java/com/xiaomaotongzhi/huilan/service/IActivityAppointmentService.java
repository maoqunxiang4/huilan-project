package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.utils.Result;

public interface IActivityAppointmentService {
    public Result addActivityAppoinment(Integer aid) ;
    public Result deleteActivityAppoinment(Integer aid) ;
    public Result showActivityAppoinment(Integer current) ;
}
