package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.jdbc.exceptions.MySQLQueryInterruptedException;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.Place;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.GymnasiumMapper;
import com.xiaomaotongzhi.huilan.mapper.PlaceMapper;
import com.xiaomaotongzhi.huilan.service.IPlaceService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Transactional
public class PlaceServiceImpl implements IPlaceService {

    @Autowired
    private PlaceMapper placeMapper ;

    @Autowired
    private GymnasiumMapper gymnasiumMapper ;

    @Override
    public Result addPlace(Integer gid , String placename , String time) {
        UserVo user = UserHolder.getUser();
        Place place = new Place();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);
        place.setGid(gid) ;
        place.setUid(user.getId());
        place.setState(0);
        place.setTime(localDateTime);
        place.setPlacename(placename);
        int rows = placeMapper.insert(place);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result updatePlace(Integer pid , Integer gid ,String placename, String time) {
        Place place = new Place();
        place.setPlacename(placename);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);
        place.setTime(localDateTime);
        int rows = placeMapper.update(place, new QueryWrapper<Place>().eq("id", pid).eq("gid",gid));
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deletePlace(Integer id) {
        int rows = placeMapper.delete(new QueryWrapper<Place>().eq("id", id));
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showPlace(Integer gid) {
        List<Place> places = placeMapper
                .selectList(new QueryWrapper<Place>()
                        .eq("gid", gid)
                        .eq("state",0)
                        .orderByAsc("time"));
        return OtherUtils.showContent(places,new Place());
    }

    @Override
    public Result searchPlace(Integer gid, String start, String end) {
        OtherUtils.NotBeNull(start,end);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime first = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime last = LocalDateTime.parse(end, dateTimeFormatter);
        List<Place> places = placeMapper.selectList(new QueryWrapper<Place>().ge("time",first).le("time",last).orderByAsc("time"));
        return OtherUtils.showContent(places,new Place());
    }


}
