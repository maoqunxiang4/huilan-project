package com.xiaomaotongzhi.huilan.service;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.vo.Jscode2session;
import org.springframework.data.redis.core.script.ReactiveScriptExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

public interface IUserService {
    public Result regist(String username , String password) ;
    public Result login( String username , String password) ;
    public Result changePassword(String oldPassword , String newPassword) ;
    public Result getAttestation(Integer attestation) ;
    public Result showAllContent(Integer current) ;
    public Result showSpecificContent(Integer id,String title) ;
    public Result getWechatInfo(String openid) ;
}
