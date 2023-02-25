package com.xiaomaotongzhi.huilan.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.PlaceAppointmentServiceImpl;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.PlaceServiceImpl;
import com.xiaomaotongzhi.huilan.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/place")
@Api(tags = "场地接口")
public class PlaceController extends BaseController {
    @Autowired
    private PlaceServiceImpl placeService ;

    @Autowired
    private PlaceAppointmentServiceImpl placeAppointmentService ;

    @GetMapping("/add")
    @ApiOperation(value = "添加场地" ,
            notes = "接口：添加场地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid" , value = "选中的场地的id（选中后自动传入）" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "placename" , value = "场地名" , dataType = "String") ,
            @ApiImplicitParam(name = "time" , value = "时间(格式:yyyy-MM-dd HH:mm)" , dataType = "LocalDateTime")
    })
    public Result addPlace(Integer gid ,String placename ,String time){
        return placeService.addPlace(gid, placename, time) ;
    }


    @GetMapping("/update")
    @ApiOperation(value = "更新场地" ,
            notes = "接口：更新场地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid" , value = "选中的场地的id（选中后自动传入）" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "gid" , value = "选中的体育馆的id（选中后自动传入）" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "placename" , value = "场地名" , dataType = "String") ,
            @ApiImplicitParam(name = "time" , value = "时间(格式:yyyy-MM-dd HH:mm)" , dataType = "LocalDateTime")
    })
    public Result updatePlace(Integer pid , Integer gid , String placename ,String time){
        return placeService.updatePlace(pid, gid ,placename, time) ;
    }


    @GetMapping("/delete")
    @ApiOperation(value = "删除场地" ,
            notes = "接口：删除场地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "选中的场地的id（选中后自动传入）" ,dataType = "Integer") ,
    })
    public Result deletePlace(Integer id){
        return placeService.deletePlace(id) ;
    }

    @GetMapping("/show")
    @ApiOperation(value = "展示场地" ,
            notes = "接口：展示场地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid" , value = "选中的体育馆的id号（选中后自动传入）" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "current" , value = "当前页数" ,dataType = "Integer") ,
    })
    public Result showPlace(Integer gid ,Integer current){
        return placeService.showPlace(gid ,current) ;
    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索场地" ,
            notes = "接口：搜索场地")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gid" , value = "选中的体育馆的id号（选中后自动传入）" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "start" , value = "初始时间(格式:yyyy-MM-dd HH:mm)" ,dataType = "String") ,
            @ApiImplicitParam(name = "end" , value = "结束时间(格式:yyyy-MM-dd HH:mm)" ,dataType = "String") ,
    })
    public Result searchPlace(Integer gid, String start, String end){
        return placeService.searchPlace(gid, start, end) ;
    }

    @GetMapping("/addPlaceAppoinment")
    @ApiOperation(value = "添加场地预约" ,
            notes = "接口：添加场地预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid" , value = "选中的场地的id号（选中后自动传入）" ,dataType = "Integer") ,
    })
    public Result addPlaceAppoinment(Integer pid){
        return placeAppointmentService.addPlaceAppoinment(pid) ;
    }
}
