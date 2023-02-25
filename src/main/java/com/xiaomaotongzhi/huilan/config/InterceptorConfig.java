package com.xiaomaotongzhi.huilan.config;

import com.xiaomaotongzhi.huilan.interceptor.LoginInterceptor;
import com.xiaomaotongzhi.huilan.interceptor.RefreshInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ArrayList<String> list = new ArrayList<>();
        list.add("/swagger**/**") ;
        list.add("/user/regist") ;
        list.add("/user/login") ;
        list.add("/user/wechatlogin/**") ;
        list.add("/v2/**") ;
        registry.addInterceptor(new RefreshInterceptor(stringRedisTemplate)).addPathPatterns("/**")
                .excludePathPatterns(list);
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(list);

    }
}
