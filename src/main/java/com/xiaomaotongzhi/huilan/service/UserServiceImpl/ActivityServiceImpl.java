package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.resource.StringResource;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.ActivityAppointment;
import com.xiaomaotongzhi.huilan.entity.ActivityContent;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.ActivityAppointmentMapper;
import com.xiaomaotongzhi.huilan.mapper.ActivityContentMapper;
import com.xiaomaotongzhi.huilan.mapper.ActivityMapper;
import com.xiaomaotongzhi.huilan.service.IActivityService;
import com.xiaomaotongzhi.huilan.utils.*;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.xiaomaotongzhi.huilan.utils.Constants.*;

//已实现缓存优化
@Service
@Transactional
public class ActivityServiceImpl implements IActivityService {
    @Autowired
    private ActivityMapper activityMapper ;

    @Autowired
    private ActivityContentMapper activityContentMapper ;

    @Autowired
    private ActivityAppointmentMapper activityAppointmentMapper ;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Override
    public Result addActivity(String title , String content , String time ) {
        OtherUtils.NotBeNull(title,content,time);

        //查看活动是否高度重合
        //如果高度重合，报错
        //不存在高度重合，不报错
        Activity isExcite = activityMapper.selectOne(new QueryWrapper<Activity>().eq("title", title));
        if (isExcite!=null){
            return Result.fail(401,"标题以存在，请更改") ;
        }
        isExcite = activityMapper.selectOne(new QueryWrapper<Activity>().eq("content", content));
        if (isExcite!=null){
            return Result.fail(401,"内容与其它活动高度重合，请更改") ;
        }

        //查看用户是否有社团认证标识
        UserVo user = UserHolder.getUser();
        //没有，拒绝访问
        if (user.getAttestation()!=1) {
            return Result.fail(401, "用户未获得认证");
        }

        //创建活动并添加活动信息
        Activity activity = new Activity();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTime_Formatter);
        LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);

        long second = localDateTime.toEpochSecond(ZoneOffset.UTC);

        activity.setTime(localDateTime);
        activity.setTitle(title);
        activity.setContent(content);
        activity.setState(0);
        activity.setLocaldatetime(second);
        activity.setUid(UserHolder.getUser().getId());

        ServiceUtils serviceUtils = new ServiceUtils();
        serviceUtils.setActivityContentMapper(activityContentMapper);
        Integer aid = serviceUtils.addActivityContent(title, user.getId(), user.getUsername(), content, 0 );

        activity.setAid(aid);
        //插入活动信息
        //插入失败，报错
        int rows = activityMapper.insert(activity);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }

        Activity one = activityMapper.selectOne(new QueryWrapper<Activity>().eq("aid", aid));
        ActivityContent activityContent = new ActivityContent();
        activityContent.setBelong(one.getId());
        activityContentMapper.update(activityContent,new QueryWrapper<ActivityContent>().eq("title",title).eq("content",content)); ;
        //插入成功，返回结果
        return Result.ok(200);
    }

    @Override
    public Result updateActivity(Integer id ,String title , String content , String time ) {
        OtherUtils.NotBeNull(id.toString(),title,content,time);
        Activity activity = new Activity();
        activity.setTitle(title);
        activity.setContent(content);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateTime_Formatter);
        LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);
        activity.setTime(localDateTime);
        int rows = activityMapper.update(activity, new QueryWrapper<Activity>().eq("id", id));
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deleteActivity(Integer id) {
        OtherUtils.NotBeNull(id.toString());
        //查看用户是否有社团认证标识
        UserVo user = UserHolder.getUser();
        //没有，拒绝访问
        if (user.getAttestation()!=1) {
            return Result.fail(401, "用户未获得认证");
        }
        //执行删除操作
        Activity activity = OtherUtils.SearchBySign(Activity.class, id, ((sign) -> activityMapper.selectById(sign)));
        Integer aid = activity.getAid();
        ServiceUtils serviceUtils = new ServiceUtils();
        serviceUtils.setActivityContentMapper(activityContentMapper);
        serviceUtils.deleteActivityContent(aid) ;
        int rows = activityMapper.delete(new QueryWrapper<Activity>().eq("id", id));
        activityAppointmentMapper.delete(new QueryWrapper<ActivityAppointment>().eq("aid",id)) ;
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showActivity(Integer current) {
        //搜索缓存
        QueryWrapper<Activity> wrapper = new QueryWrapper<Activity>().orderByAsc("localdatetime");
        Page<Activity> page = new Page<>((long)DEFAULT_PAGESIZE * (current-1),DEFAULT_PAGESIZE );
        Page<Activity> activityPage = activityMapper.selectPage(page, wrapper);
        return OtherUtils.showContent(activityPage.getRecords(),new Activity());
    }

}
