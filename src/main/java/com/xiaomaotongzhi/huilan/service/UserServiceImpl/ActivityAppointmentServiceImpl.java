package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.ActivityAppointment;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.ActivityAppointmentMapper;
import com.xiaomaotongzhi.huilan.mapper.ActivityMapper;
import com.xiaomaotongzhi.huilan.service.IActivityAppointmentService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ActivityAppointmentServiceImpl implements IActivityAppointmentService {
    @Autowired
    private ActivityAppointmentMapper activityAppoinmentMapper ;

    @Autowired
    private ActivityMapper activityMapper ;

    @Override
    public Result addActivityAppoinment(Integer aid ) {
        OtherUtils.NotBeNull(aid.toString());
        UserVo user = UserHolder.getUser();
        ActivityAppointment activityAppoinment = new ActivityAppointment();
        activityAppoinment.setAid(aid);
        activityAppoinment.setUid(user.getId());
        activityAppoinment.setSign(1);
        int rows = activityAppoinmentMapper.insert(activityAppoinment);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deleteActivityAppoinment(Integer aid) {
        OtherUtils.NotBeNull(aid.toString());
        int rows = activityAppoinmentMapper.delete(new QueryWrapper<ActivityAppointment>().eq("aid",aid));
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    //query()方法实现排序展示
    @Override
    public Result showActivityAppoinment() {
        ArrayList<Integer> ids = new ArrayList<>();
        List<ActivityAppointment> activityAppoinments =
                activityAppoinmentMapper.selectList
                        (new QueryWrapper<ActivityAppointment>()
                                .eq("uid",UserHolder.getUser().getId())
                                .eq("state",0)
                                .orderByDesc("time"));
        if (activityAppoinments.isEmpty()){
            return Result.ok(200,new Activity()) ;
        }

        for (ActivityAppointment activityAppoinment : activityAppoinments) {
            ids.add(activityAppoinment.getAid()) ;
        }

        List<Activity> activities = activityMapper.selectBatchIds(ids);
        return Result.ok(200,activities);
    }
}
