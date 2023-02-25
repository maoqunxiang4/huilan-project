package com.xiaomaotongzhi.huilan.controller;

import com.xiaomaotongzhi.huilan.service.UserServiceImpl.LetterServiceImpl;
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
@RequestMapping("/letter")
@Api(tags = "收件箱接口")
public class LetterConroller {

    @Autowired
    private LetterServiceImpl letterService ;

    @GetMapping("/show")
    @ApiOperation(value = "展示邮件" ,
            notes = "接口：展示邮件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current" , value = "当前页数" ,dataType = "Integer") ,
    })
    private Result showLetters(Integer current){
        return letterService.showLetter(current) ;
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除邮件" ,
            notes = "接口：删除邮件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "邮件id(选中后自动传入)" , dataType = "Integer")
    })
    private Result deleteLetter(Integer id){
        return letterService.deleteLetter(id) ;
    }

    //TODO 发送邮件

    //删除大量文件
//    @RequestMapping("/deletes")
//    private Result deleteLetters(){
//        return letterService.showLetter() ;
//    }
}
