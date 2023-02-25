package com.xiaomaotongzhi.huilan.controller;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.xiaomaotongzhi.huilan.entity.DoubleComment;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.CommentServiceImpl;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.DoubleCommentServiceImpl;
import com.xiaomaotongzhi.huilan.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@Api(tags = "社团评论接口")
public class ConmmentController {
    @Autowired
    private CommentServiceImpl commentService ;

    @GetMapping("/add")
    @ApiOperation(value = "添加评论" ,
            notes = "接口：添加评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid" , value = "评论所属的社团博客id" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "content" , value = "评论的内容" , dataType = "String")
    })
    public Result addComment(Integer cid , String content){
        return commentService.addComment(cid, content) ;
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除评论" ,
            notes = "接口：删除评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "评论id号" ,dataType = "Integer")
    })
    public Result deleteComment(Integer id){
        return commentService.deleteComment(id) ;
    }

    @GetMapping("/show")
    @ApiOperation(value = "展示评论" ,
            notes = "接口：展示评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid" , value = "社团博客的id号" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "current" , value = "当前页数" ,dataType = "Integer") ,
    })
    public Result showComments(Integer cid , Integer current){
        return commentService.showComments(cid,current) ;
    }


}
