package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.resource.StringResource;
import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.ActivityContent;
import com.xiaomaotongzhi.huilan.entity.ClubContent;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.ActivityContentMapper;
import com.xiaomaotongzhi.huilan.mapper.ActivityMapper;
import com.xiaomaotongzhi.huilan.mapper.ClubContentMapper;
import com.xiaomaotongzhi.huilan.mapper.UserMapper;
import com.xiaomaotongzhi.huilan.service.IUserService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.RedisUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.Jscode2session;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.xiaomaotongzhi.huilan.utils.Constants.LOGIN_PREFIX;
import static com.xiaomaotongzhi.huilan.utils.Constants.WECHAT_PREFIX;
import static com.xiaomaotongzhi.huilan.utils.RedisUtils.filedValueToString;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private ActivityMapper activityMapper ;

    @Autowired
    private ActivityContentMapper activityContentMapper ;

    @Autowired
    private ClubContentMapper clubContentMapper ;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Override
    public Result regist(String username ,String password) {
        //查看用户是否存在
        if (username==null) return Result.fail(403,"用户名不能为空") ;
        User isExicte = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        //存在，报错
        if (isExicte!=null){
            return Result.fail(400,"用户已存在") ;
        }
        //不存在，继续执行
        //获取盐值
        String salt = UUID.randomUUID().toString().toUpperCase();
        //密码加密
        String md5Password = getMd5Password(password, salt);
        //存入数据
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password);
        user.setSalt(salt);
        user.setIsDelete(0);
        user.setAttestation(0);
        //进行插入操作
        int rows = userMapper.insert(user);
        //看插入是否成功,不成功，报错
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }

        //成功，继续执行，返回结果
        return Result.ok(200);
    }

    @Override
    public Result login(String username , String password) {
        //查看用户数据是否存在
        User isExicte = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        //不存在，报错
        if (isExicte==null){
            return Result.fail(401,"该用户未完成注册") ;
        }
        //存在，继续执行
        //查看密码是否正确,不正确，报错
        String rightPassword = isExicte.getPassword();
        String md5Password = getMd5Password(password, isExicte.getSalt());
        if (!rightPassword.equals(md5Password)){
            return Result.fail(401,"密码错误") ;
        }
        //存入redis中
        String s = RandomUtil.randomString(6);
        while(s.isEmpty()){
            s = RandomUtil.randomString(6);
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(isExicte,userVo);
        userVo.setToken(s);
        stringRedisTemplate.opsForHash().putAll(
                LOGIN_PREFIX + s ,
                BeanUtil.beanToMap(userVo,
                        new HashMap<>(),
                        CopyOptions.create().
                                setIgnoreNullValue(false).
                                setFieldValueEditor((filedName,filedValue)-> filedValueToString(filedValue))));
        stringRedisTemplate.expire(LOGIN_PREFIX + s , 30 ,TimeUnit.MINUTES) ;
        //正确，继续执行，返回结果
        return Result.ok(200,userVo);
    }

    @Override
    public Result changePassword(String oldPassword ,String newPassword) {
        if (oldPassword==newPassword){
            return Result.fail(401,"新旧密码不能相同，请重试") ;
        }
        //检查密码是否正确
        UserVo userVo = UserHolder.getUser();
        User user = userMapper.selectById(userVo.getId());
        String password = user.getPassword();
        //不正确，报错
        if (!password.equals(getMd5Password(oldPassword, user.getSalt()))){
            return Result.fail(401,"密码有误") ;
        }
        //正确，继续执行
        //更改密码
        User entity = new User();
        entity.setPassword(getMd5Password(newPassword, user.getSalt()));
        int rows = userMapper.update(entity, new QueryWrapper<User>().eq("username", user.getUsername()));
        //查看密码是否成功更改
        //不成功，报错
        if (rows==0) {
            return Result.fail(503, "服务器出现不知名异常，请稍后重试");
        }
        //成功，返回结果
        return Result.ok(200) ;
    }

    @Override
    public Result getAttestation(Integer attestation) {
        User user = new User();
        user.setAttestation(attestation);
        int rows = userMapper.update(user, new QueryWrapper<User>().eq("username", UserHolder.getUser().getUsername()));
        if (rows == 0) {
            return Result.fail(503 , "服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }


    @Override
    public Result showAllContent(){
        UserVo user = UserHolder.getUser();
        Integer id = user.getId();
        Integer attestation = user.getAttestation();
        if (attestation==1){
            List<Activity> activities = activityMapper
                    .selectList(new QueryWrapper<Activity>().eq("id", id)
                            .orderByAsc("time"));
            return  OtherUtils.showContent(activities,new Activity()) ;
        }else if (attestation==2){
            List<ClubContent> clubContents = clubContentMapper
                    .selectList(new QueryWrapper<ClubContent>().eq("id", id)
                            .orderByAsc("time"));
            return OtherUtils.showContent(clubContents,new ClubContent()) ;
        }
        return Result.ok(200) ;
    }

    @Override
    public Result showSpecificContent(Integer id){
        Integer attestation = UserHolder.getUser().getAttestation();
        if (attestation==1){
            ActivityContent activityContent = activityContentMapper.selectById(id);
            if (activityContent!=null){
                return Result.ok(200,activityContent) ;
            }
        }else if (attestation==2){
            ClubContent clubContent = clubContentMapper.selectById(id);
            if (clubContent==null){
                return Result.ok(200,clubContent) ;
            }
        }
        return Result.ok(200) ;
    }

    @Override
    public Result wechatLogin(Jscode2session jscode2session ,String username) {
        String openid = DigestString(jscode2session.getOpenid());
        String session_key = DigestString(jscode2session.getSession_key());
        jscode2session.setOpenid(openid);
        jscode2session.setSession_key(session_key);

        User user = new User();
        User isUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (isUser==null){
            user.setUsername(username);
            user.setIsDelete(0);
            user.setAttestation(0);
            int rows = userMapper.insert(user);
            if (rows==0){
                return Result.fail(401,"服务器出现不知名异常") ;
            }
        }

        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user,userVo) ;
        userVo.setSession_key(session_key);
        userVo.setOpenId(openid);
        Map<String, Object> map = BeanUtil.beanToMap(userVo, new HashMap<>()
                , CopyOptions.create().setIgnoreNullValue(true)
                        .setFieldValueEditor((filedName, filedValue) -> filedValueToString(filedValue)));
        stringRedisTemplate.opsForHash().putAll(WECHAT_PREFIX + openid , map);
        return Result.ok(200,userVo);
    }

    protected String getMd5Password(String password , String  salt) {
        for (int i = 0 ; i<3 ; i++ ) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password ;
    }

    public static String DigestString(String str ){
        for (int i = 0 ; i<3 ;i++){
            str = DigestUtils.md5DigestAsHex(("ABC" + str + "ABC").getBytes()).toUpperCase() ;
        }
        return  str ;
    }
}
