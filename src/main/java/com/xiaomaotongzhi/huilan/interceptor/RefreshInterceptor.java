package com.xiaomaotongzhi.huilan.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.UserServiceImpl;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.xiaomaotongzhi.huilan.utils.Constants.LOGIN_PREFIX;
import static com.xiaomaotongzhi.huilan.utils.Constants.WECHAT_PREFIX;

public class RefreshInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate ;

    public RefreshInterceptor(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate ;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            //检查用户是否登录过期
            String token = request.getHeader("token");
            Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(LOGIN_PREFIX + token);

            //过期，进行拦截
            if (map.isEmpty()){
                return false ;
            }

            //不过期，继续执行并刷新缓存
            stringRedisTemplate.expire(LOGIN_PREFIX + token , 30 , TimeUnit.MINUTES) ;
            UserVo user = BeanUtil.fillBeanWithMap(map,new UserVo(), false);
            UserHolder.setUser(user);
            return true ;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
