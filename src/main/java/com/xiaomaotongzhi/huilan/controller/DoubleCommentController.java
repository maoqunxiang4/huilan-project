package com.xiaomaotongzhi.huilan.controller;

import com.xiaomaotongzhi.huilan.service.UserServiceImpl.CommentServiceImpl;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.DoubleCommentServiceImpl;
import com.xiaomaotongzhi.huilan.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doublecomment")
@Api(tags = "回复接口")
public class DoubleCommentController {
    @Autowired
    private DoubleCommentServiceImpl doubleCommentService ;

    @GetMapping("/add")
    @ApiOperation(value = "添加回复" ,
            notes = "接口：添加回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comid" , value = "评论所属的评论id" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "content" , value = "评论的内容" , dataType = "String")
    })
    public Result addComment(Integer comid , String content){
        return doubleCommentService.addComment(comid, content) ;
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除回复" ,
            notes = "接口：删除回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "评论中的评论id号" ,dataType = "Integer")
    })
    public Result deleteComment(Integer id){
        return doubleCommentService.deleteComment(id) ;
    }

    @GetMapping("/show")
    @ApiOperation(value = "展示回复" ,
            notes = "接口：展示回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comid" , value = "所处评论的id号" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "current" , value = "当前页数" ,dataType = "Integer") ,
    })
    public Result showComments(Integer comid , Integer current){
        return doubleCommentService.showComments(comid ,current) ;
    }
}
