package com.xiaomaotongzhi.huilan.controller;

import com.xiaomaotongzhi.huilan.service.UserServiceImpl.ActivityAppointmentServiceImpl;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.ActivityServiceImpl;
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
@RequestMapping("/activity")
@Api(tags = "活动接口")
public class ActivityController {
    @Autowired
    private ActivityServiceImpl activityService ;

    @Autowired
    private ActivityAppointmentServiceImpl activityAppoinmentService ;

    @GetMapping("/add")
    @ApiOperation(value = "增加活动" ,
                notes = "接口：增加活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title" , value = "标题" , dataType = "String") ,
            @ApiImplicitParam(name = "content" , value ="活动内容" , dataType = "String" ) ,
            @ApiImplicitParam(name = "time" , value = "活动时间(格式:yyyy-MM-dd HH:mm)" , dataType = "String")
    })
    //这个地方，我们需要改成，自己添加完一个活动的项目之后，再点这个活动，就可以向里面添加内容了
    public Result addActivity(String title , String content , String time){
        return activityService.addActivity(title, content, time) ;
    }

    @GetMapping("/update")
    @ApiOperation(value = "更新活动" ,
            notes = "接口：更新活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "需要更改的活动的id（选中后自动传入）" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "title" , value = "标题" , dataType = "String") ,
            @ApiImplicitParam(name = "content" , value ="活动内容" , dataType = "String" ) ,
            @ApiImplicitParam(name = "time" , value = "活动时间(格式:yyyy-MM-dd HH:mm)" , dataType = "LocalDateTime")
    })
    public Result updateActivity(Integer id ,String title , String content , String time){
        return activityService.updateActivity(id, title, content, time) ;
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除活动" ,
                    notes= "接口：删除活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id" , value = "活动id" , dataType="Integer")
    })
    public Result deleteActivity(Integer id){
        return activityService.deleteActivity(id) ;
    }

    @GetMapping("/show")
    @ApiOperation(value = "展示活动" ,
            notes = "接口：展示活动")
    public Result showActivity(){
        return activityService.showActivity() ;
    }

    @GetMapping("/appoinment")
    @ApiOperation(value = "增加活动预约接口" ,
            notes = "接口：增加活动预约接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aid" , value = "活动id(前端选中后自动传入)" , dataType = "Integer")
    })
    public Result addActivityAppoinment(Integer aid){
        return activityAppoinmentService.addActivityAppoinment(aid) ;
    }
}
