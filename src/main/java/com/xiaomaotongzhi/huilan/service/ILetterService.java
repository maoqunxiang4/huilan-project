package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.utils.Result;
import io.swagger.models.auth.In;

public interface ILetterService {
    Result showLetter(Integer current) ;
    Result isRead(Integer id) ;
    Result deleteLetter(Integer id) ;
}
