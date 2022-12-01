package com.xiaomaotongzhi.huilan.aop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.HashUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.ActivityContentMapper;
import com.xiaomaotongzhi.huilan.mapper.ActivityMapper;
import com.xiaomaotongzhi.huilan.mapper.UserMapper;
import com.xiaomaotongzhi.huilan.utils.RedisUtils;
import com.xiaomaotongzhi.huilan.utils.ServiceUtils;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.exception.ServiceException;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.xiaomaotongzhi.huilan.utils.Constants.LOGIN_PREFIX;

@Aspect
@Configuration
public class CheckAop {
    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private ActivityContentMapper activityContentMapper ;

    @Autowired
    private ActivityMapper activityMapper ;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Before("execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..*.*(..)) && !execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..UserServiceImpl.login(..)) && !execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..UserServiceImpl.regist(..))")
    public void BeforeForUser() throws Throwable {
        Integer id = UserHolder.getUser().getId();
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", id));
        if (user.getIsDelete()==1){
            throw new ServiceException("用户不存在") ;
        }
    }

    @Before("execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..GymnasiumServiceImpl.*(..)) && !execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..GymnasiumServiceImpl.showGymnasium(..))")
    public void BeforeForGymnasium() throws Throwable {
        User user = userMapper.selectById(UserHolder.getUser().getId());
        if (user.getAttestation()!=2){
            throw new ServiceException("该用户未取得认证，请稍后重试") ;
        }
    }

    @Before("execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..ActivityServiceImpl.*(..)) && !execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..ActivityServiceImpl.showActivity(..))")
    public void BeforeForActivity() throws Throwable {
        User user = userMapper.selectById(UserHolder.getUser().getId());
        if (user.getAttestation()!=1){
            throw new ServiceException("该用户未取得认证，请稍后重试") ;
        }
    }

    @Before("execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..ClubContentServiceImpl.*(..)) && !execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..ClubContentServiceImpl.showAllClubContent(..))")
    public void BeforeCheckAttestation(){
        Integer attestation = UserHolder.getUser().getAttestation();
        if (attestation!=1){
            throw new ServiceException("该用户未取得认证，请稍后重试") ;
        }
    }
}
