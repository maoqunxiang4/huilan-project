package com.xiaomaotongzhi.huilan.service;

import com.xiaomaotongzhi.huilan.utils.Result;

import java.awt.geom.RectangularShape;

public interface ICommentService {
    Result addComment(Integer cid , String content) ;
    Result deleteComment(Integer id) ;
    Result showComments(Integer cid) ;
}
