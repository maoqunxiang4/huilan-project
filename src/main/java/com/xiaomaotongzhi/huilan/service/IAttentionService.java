package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.utils.Result;

import javax.jws.soap.SOAPBinding;

public interface IAttentionService {
    Result addOrDeleteAttention(Integer uid) ;
    Result showAttentionList() ;
    User isAttention(User user) ;
}
