package com.xiaomaotongzhi.huilan.controller;

import com.xiaomaotongzhi.huilan.mapper.GymnasiumMapper;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.GymnasiumServiceImpl;
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
@RequestMapping("/Gymnasium")
@Api(tags = "体育馆接口")
public class GymnasiumController extends BaseController{
    @Autowired
    private GymnasiumServiceImpl gymnasiumService ;

    @GetMapping("/add")
    @ApiOperation(value = "添加体育馆" ,
            notes = "接口：添加体育馆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name" , value = "体育馆名" , dataType = "String")
    })
    public Result addGymnasium( String name ){
        return gymnasiumService.addGymnasium(name) ;
    }


    @GetMapping("/update")
    @ApiOperation(value = "更新体育馆" ,
            notes = "接口：更新体育馆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "选中的体育馆的id（选中后自动传入）" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "name" , value = "体育馆名" , dataType = "String")
    })
    public Result updateGymnasium(Integer id , String name){
        return gymnasiumService.updateGymnasium(id, name) ;
    }


    @GetMapping("/delete")
    @ApiOperation(value = "删除体育馆" ,
            notes = "接口：删除体育馆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "选中的体育馆的id（选中后自动传入）" ,dataType = "Integer") ,
    })
    public Result deleteGymnasium(Integer id){
        return gymnasiumService.deleteGymnasium(id) ;
    }

    @GetMapping("/show")
    @ApiOperation(value = "展示体育馆" ,
            notes = "接口：展示体育馆")
    public Result showGymnasium(){
        return gymnasiumService.showGymnasium() ;
    }

}
