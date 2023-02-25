package com.xiaomaotongzhi.huilan.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.xiaomaotongzhi.huilan.entity.DoubleComment;
import com.xiaomaotongzhi.huilan.mapper.ClubContentMapper;
import com.xiaomaotongzhi.huilan.mapper.CommentMapper;
import com.xiaomaotongzhi.huilan.mapper.DoubleCommentMapper;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.ClubContentServiceImpl;
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
@RequestMapping("/clubcontent")
@Api(tags = "科技社团接口")
public class ClubContentController extends BaseController {
    @Autowired
    private ClubContentServiceImpl clubContentService ;

    @Autowired
    private CommentServiceImpl commentService ;

    @Autowired
    private DoubleCommentServiceImpl doubleCommentService ;

    @GetMapping("/add")
    @ApiOperation(value = "添加社团博客" ,
            notes = "接口：添加社团博客")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title" , value = "标题" ,dataType = "String") ,
            @ApiImplicitParam(name = "content" , value = "正文" , dataType = "String")
    })
    public Result addClubContent(String title ,String content){
        return clubContentService.addClubContent(title,content) ;
    }

    @GetMapping("/update")
    @ApiOperation(value = "更新社团博客" ,
            notes = "接口：更新社团博客")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "选中的社团博客id" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "title" , value = "标题" , dataType = "String") ,
            @ApiImplicitParam(name = "content" , value = "正文" , dataType = "String")
    })
    public Result updateClubContent(Integer id ,String title ,String content){
        return clubContentService.updateClubContent(id, title, content);
    }

    @ApiOperation(value = "删除社团博客" ,
            notes = "接口：删除社团博客")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "社团博客id" , dataType = "Integer")
    })
    @GetMapping("/delete")
    public Result deleteClubContent(Integer id) {
        return clubContentService.deleteClubContent(id) ;
    }

    @ApiOperation(value = "展示全部社团博客（只需展示标题）" ,
            notes = "接口：展示全部社团博客（只需展示标题）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current" , value = "当前页数" ,dataType = "Integer") ,
    })
    @GetMapping("/show")
    public Result showClubContent(Integer current){
        return clubContentService.showAllClubContent(current) ;
    }


    @GetMapping("/showSpecific")
    @ApiOperation(value = "展示具体社团博客内容" ,
            notes = "接口：展示具体社团内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "社团博客id" , dataType = "Integer")
    })
    public Result showSpecificClubContent(Integer id){
        return clubContentService.showSpecificClubContent(id) ;
    }

    @GetMapping("/islike")
    @ApiOperation(value = "点赞和取消点赞" ,
            notes = "接口：点赞和取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "社团博客id" , dataType = "Integer")
    })
    public Result changeLike(Integer id){
        return clubContentService.changeLike(id) ;
    }
}
