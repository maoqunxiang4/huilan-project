package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.Gymnasium;
import com.xiaomaotongzhi.huilan.entity.Place;
import com.xiaomaotongzhi.huilan.mapper.GymnasiumMapper;
import com.xiaomaotongzhi.huilan.service.IGymnasiumService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.xiaomaotongzhi.huilan.utils.Constants.DEFAULT_PAGESIZE;

@Service
@Transactional
public class GymnasiumServiceImpl implements IGymnasiumService {
    @Autowired
    private GymnasiumMapper gymnasiumMapper ;

    @Override
    public Result addGymnasium( String name) {
        Gymnasium gymnasium = new Gymnasium();
        gymnasium.setUid(UserHolder.getUser().getId());
        gymnasium.setName(name);
        int rows = gymnasiumMapper.insert(gymnasium);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result updateGymnasium(Integer id, String name) {
        Gymnasium gymnasium = new Gymnasium();
        gymnasium.setUid(UserHolder.getUser().getId());
        gymnasium.setName(name) ;
        int rows = gymnasiumMapper.update(gymnasium, new QueryWrapper<Gymnasium>().eq("id", id));
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deleteGymnasium(Integer id) {
        int rows = gymnasiumMapper.delete(new QueryWrapper<Gymnasium>().eq("id", id));
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showGymnasium(Integer current) {
        Page<Gymnasium> page = new Page<>((long) DEFAULT_PAGESIZE * (current - 1), DEFAULT_PAGESIZE);
        Page<Gymnasium> gymnasiumPage = gymnasiumMapper.selectPage(page, new QueryWrapper<Gymnasium>());
        return OtherUtils.showContent(gymnasiumPage.getRecords(),new Gymnasium()) ;
    }
}
