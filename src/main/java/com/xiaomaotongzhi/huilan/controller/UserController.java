package com.xiaomaotongzhi.huilan.controller;

import cn.hutool.json.JSONUtil;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.ActivityAppointmentServiceImpl;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.AttentionServiceImpl;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.PlaceAppointmentServiceImpl;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.UserServiceImpl;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.vo.Jscode2session;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

import static com.xiaomaotongzhi.huilan.utils.Constants.WECHAT_PREFIX;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关操作接口")
public class UserController extends BaseController{


    @Value("${wechat.appid}")
    private String appid ;

    @Value("${wechat.secret}")
    private String secret ;

    @Autowired
    private RestTemplate restTemplate ;

    @Autowired
    private UserServiceImpl userService ;

    @Autowired
    private ActivityAppointmentServiceImpl activityAppoinmentService ;

    @Autowired
    private AttentionServiceImpl attentionService ;

    @Autowired
    private PlaceAppointmentServiceImpl placeAppoinmentService ;

    @ApiOperation(value = "微信登陆" ,
            notes = " 描述：微信登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code" , value = "wx登陆code" , dataType = "String") ,
    })
    @GetMapping("/wechatlogin/{code}")
    public Result wechatLogin(@PathVariable(name = "code") String code){
        String path = "https://api.weixin.qq.com/sns/jscode2session?appid=" +
                appid +  "&secret=" + secret +"&js_code=" + code +  "&grant_type=authorization_code" ;
        String json = restTemplate.getForObject(path, String.class);
        Jscode2session jscode2session = JSONUtil.toBean(json, Jscode2session.class) ;
        userService.getWechatInfo(jscode2session.getOpenid());
        return Result.ok(200,jscode2session) ;
    }

    @GetMapping("/regist")
    @ApiOperation(value = "注册" ,
                    notes = " 描述：用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username" , value = "用户名" , dataType = "String") ,
            @ApiImplicitParam(name = "password" , value = "密码" , dataType = "String")
    })
    public Result regist(String username ,String password){
        return userService.regist(username, password) ;
    }

    @GetMapping("/login")
    @ApiOperation(value = "登陆" ,
                    notes = "描述：用户登陆 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username" , value = "用户名" , dataType = "String") ,
            @ApiImplicitParam(name = "password" , value = "密码" , dataType = "String")
    })
    public Result login( String username , String password){
        return userService.login( username , password);
    }

    @GetMapping("/change")
    @ApiOperation(value = "更改密码" ,
                    notes = "接口：更改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="oldPassword" , value = "旧密码" , dataType = "String") ,
            @ApiImplicitParam(name="newPassword" , value = "新密码" , dataType = "String")
    })
    public Result changePassword(String oldPassword,String newPassword){
        return userService.changePassword(oldPassword,newPassword) ;
    }

    @GetMapping("/attestation")
    @ApiOperation(value="获取认证" ,
                    notes="接口：获取认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attestation" , value = "认证" , dataType = "String")
    })
    public Result getAttestation(Integer attestation){
        return userService.getAttestation(attestation) ;
    }

    @ApiOperation(value="展示预约" ,
            notes="接口：展示预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sign" , value = "标志符(传入1表示文体预约，传入2表示场地预约)" ,dataType = "Integer") ,
            @ApiImplicitParam(name = "current" , value = "当前页" ,dataType = "Integer")
    })
    @GetMapping("/show/appointment")
    public Result showAppointment(Integer sign ,Integer current){
        if (sign==1) return activityAppoinmentService.showActivityAppoinment(current) ;
        else return placeAppoinmentService.showPlaceAppoinment(current);
    }

    @ApiOperation(value = "取消体育馆场地预约" ,
            notes = "接口：取消体育馆场地预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid" , value = "场地id(前端选中后自动传入)" , dataType = "Integer")
    })
    @GetMapping("/delete/place/appoinment")
    public Result deletePlaceAppointment(Integer pid){
        return placeAppoinmentService.deletePlaceAppoinment(pid) ;
    }

    @ApiOperation(value = "取消文体活动预约" ,
            notes = "接口：取消文体活动预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aid" , value = "活动id(前端选中后自动传入)" , dataType = "Integer")
    })
    @GetMapping("/delete/activity/appoinment")
    public Result deleteActivityAppointment(Integer aid){
        return activityAppoinmentService.deleteActivityAppoinment(aid) ;
    }

    @ApiOperation(value = "展示全部博客（只展示标题）" ,
            notes = "接口：展示全部博客（只展示标题）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current" , value = "当前页" ,dataType = "Integer")
    })
    @GetMapping("/show/allContent")
    public Result showAllContent(Integer current){
        return userService.showAllContent(current) ;
    }

    @ApiOperation(value = "展示博客具体内容" ,
            notes = "接口：展示博客具体内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "指定博客的id" , dataType = "Integer") ,
            @ApiImplicitParam(name = "title" , value = "标题" , dataType = "String") ,
    })
    @GetMapping("/delete/specificContent")
    public Result showSpecificContent(Integer id,String title){
        return userService.showSpecificContent(id, title) ;
    }

    @ApiOperation(value = "关注或取关" ,
            notes = "接口：关注或取关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid" , value = "想要关注或取关的用户的id号" , dataType = "Integer")
    })
    @GetMapping("/attention")
    public Result addOrDeleteAttention(Integer uid){
        return attentionService.addOrDeleteAttention(uid) ;
    }

    @ApiOperation(value = "关注列表" ,
            notes = "接口：关注列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current" , value = "当前页" ,dataType = "Integer")
    })
    @GetMapping("/show/attentionList")
    public Result showAttentionList(Integer current){
        return attentionService.showAttentionList(current) ;
    }
}
