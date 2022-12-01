package com.xiaomaotongzhi.huilan.aop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.UserMapper;
import com.xiaomaotongzhi.huilan.utils.RedisUtils;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.xiaomaotongzhi.huilan.utils.Constants.LOGIN_PREFIX;

@Aspect
@Configuration
public class RefreshAop {
    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @After("execution(* com.xiaomaotongzhi.huilan.service.UserServiceImpl..UserServiceImpl.getAttestation(..))")
    public void AfterRefreshCache() {
        User user = userMapper.selectById(UserHolder.getUser().getId());
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        userVo.setToken(UserHolder.getUser().getToken());
        Map<String, Object> map = BeanUtil.beanToMap(userVo, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(false)
                        .setFieldValueEditor((filedName, filedValue) -> RedisUtils.filedValueToString(filedValue)
                        ));
        stringRedisTemplate.opsForHash().putAll(LOGIN_PREFIX + UserHolder.getUser().getToken() , map );
    }

}
