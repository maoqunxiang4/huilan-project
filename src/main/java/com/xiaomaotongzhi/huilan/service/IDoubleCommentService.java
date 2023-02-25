package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.utils.Result;

public interface IDoubleCommentService {
    Result addComment(Integer comid , String content) ;
    Result deleteComment(Integer id) ;
    Result showComments(Integer comid ,Integer current) ;
}
