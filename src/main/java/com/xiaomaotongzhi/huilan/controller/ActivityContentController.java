package com.xiaomaotongzhi.huilan.controller;

import com.xiaomaotongzhi.huilan.entity.ActivityContent;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.ActivityContentServiceImpl;
import com.xiaomaotongzhi.huilan.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/activityContent")
@Api(tags = "活动内容接口")
public class ActivityContentController {
    @Autowired
    private ActivityContentServiceImpl activityContentService;

//    @GetMapping("/add")
//    public Result addActivityContent(String title , String content , LocalDateTime time){
//        return Result.ok(200) ;
//    }

    @GetMapping("/update")
    @ApiOperation(value = "更新活动内容" ,
            notes = "接口：更新活动内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "选中的活动内容id号" , dataType = "Integer") ,
            @ApiImplicitParam(name = "title" , value = "活动标题" , dataType = "String") ,
            @ApiImplicitParam(name = "content" , value ="活动内容" , dataType = "String" ) ,
    })
    public Result updateActivityContent(Integer id ,String title , String content){
        return  activityContentService.updateActivityContent(id,title,content);
    }


//    @GetMapping("/delete")
//    @ApiOperation(value = "删除活动接口" ,
//            notes = "<span style='color:red;'>接口：</span>&nbsp删除活动接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id" , value = "活动内容id" , dataType = "Integer")
//    })
//    public Result deleteActivityContent(Integer id){
//        return  Result.ok(200);
//    }


    @GetMapping("/activity")
    @ApiOperation(value = "展示活动内容" ,
            notes = "接口：展示活动内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "活动内容id" , dataType = "Integer")
    })
    public Result showActivityContent(Integer id ){
        return  activityContentService.showActivityContent(id);
    }

}
