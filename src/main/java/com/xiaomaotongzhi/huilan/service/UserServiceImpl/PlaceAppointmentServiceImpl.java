package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.query;
import static com.xiaomaotongzhi.huilan.utils.Constants.DEFAULT_PAGESIZE;

@Service
@Transactional
public class PlaceAppointmentServiceImpl implements IPlaceAppointmentService {

    @Autowired
    private PlaceAppointmentMapper placeAppointmentMapper ;

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
        int rows = placeAppointmentMapper.insert(placeAppoinment);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deletePlaceAppoinment(Integer pid) {
        OtherUtils.NotBeNull(pid.toString());
        int rows = placeAppointmentMapper.delete(new QueryWrapper<PlaceAppointment>().eq("pid",pid));
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showPlaceAppoinment(Integer current ) {
        ArrayList<Integer> ids = new ArrayList<>();
        UserVo user = UserHolder.getUser();
        QueryWrapper<PlaceAppointment> wrapper = new QueryWrapper<PlaceAppointment>()
                .eq("uid", user.getId()).eq("state", 0).orderByDesc("time");
        Page<PlaceAppointment> page = new Page<>((long) DEFAULT_PAGESIZE * (current-1), DEFAULT_PAGESIZE);
        Page<PlaceAppointment> placeAppointmentPage = placeAppointmentMapper.selectPage(page, wrapper);
        List<PlaceAppointment> placeAppointments = placeAppointmentPage.getRecords();
        if (placeAppointments.isEmpty()){
            return Result.ok(200,new Place()) ;
        }

        for (PlaceAppointment placeAppoinment : placeAppointments) {
            ids.add(placeAppoinment.getId()) ;
        }
        List<Place> places = placeMapper.selectBatchIds(ids);
        return Result.ok(200,places);
    }
}
