package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomaotongzhi.huilan.entity.*;
import com.xiaomaotongzhi.huilan.mapper.PlaceAppointmentMapper;
import com.xiaomaotongzhi.huilan.mapper.PlaceMapper;
import com.xiaomaotongzhi.huilan.service.IPlaceAppointmentService;
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
public class PlaceAppointmentServiceImpl implements IPlaceAppointmentService {

    @Autowired
    private PlaceAppointmentMapper placeAppoinmentMapper ;

    @Autowired
    private PlaceMapper placeMapper ;

    @Override
    public Result addPlaceAppoinment(Integer pid) {
        OtherUtils.NotBeNull(pid.toString());
        UserVo user = UserHolder.getUser();
        PlaceAppointment placeAppoinment = new PlaceAppointment();
        placeAppoinment.setUid(user.getId());
        placeAppoinment.setPid(pid);
        placeAppoinment.setSign(2);
        Place place = placeMapper.selectById(pid);
        placeAppoinment.setState(0);
        placeAppoinment.setTime(place.getTime());
        int rows = placeAppoinmentMapper.insert(placeAppoinment);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deletePlaceAppoinment(Integer pid) {
        OtherUtils.NotBeNull(pid.toString());
        int rows = placeAppoinmentMapper.delete(new QueryWrapper<PlaceAppointment>().eq("pid",pid));
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showPlaceAppoinment() {
        ArrayList<Integer> ids = new ArrayList<>();
        UserVo user = UserHolder.getUser();
        List<PlaceAppointment> placeAppoinments =
                placeAppoinmentMapper
                        .selectList(new QueryWrapper<PlaceAppointment>()
                                .eq("uid", user.getId()).eq("state",0).orderByDesc("time"));
        if (placeAppoinments.isEmpty()){
            return Result.ok(200,new Place()) ;
        }

        for (PlaceAppointment placeAppoinment : placeAppoinments) {
            ids.add(placeAppoinment.getId()) ;
        }
        List<Place> places = placeMapper.selectBatchIds(ids);
        return Result.ok(200,places);
    }
}
