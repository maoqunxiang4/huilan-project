package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.ActivityContent;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.ActivityContentMapper;
import com.xiaomaotongzhi.huilan.mapper.ActivityMapper;
import com.xiaomaotongzhi.huilan.service.IActivityContentService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.RedisUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.exception.ServiceException;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.xiaomaotongzhi.huilan.utils.Constants.*;

//已实现缓存优化
@Service
@Transactional
public class ActivityContentServiceImpl implements IActivityContentService {
    @Autowired
    private ActivityContentMapper activityContentMapper ;

    @Autowired
    private ActivityMapper activityMapper ;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Override
    public Result updateActivityContent(Integer id , String title , String content) {
        OtherUtils.NotBeNull(title,content);
        OtherUtils.SearchBySign(ActivityContent.class,id,(sign->activityContentMapper.selectById(sign) ));
        //查看用户是否有社团认证标识
        UserVo user = UserHolder.getUser();
        //没有，拒绝访问
        if (user.getAttestation()!=1) {
            return Result.fail(401, "用户为获得认证");
        }
        //有，继续执行
        //存储数据
        ActivityContent activityContent = new ActivityContent();
        activityContent.setId(id);
        activityContent.setContent(content);
        activityContent.setTitle(title);
        //添加数据到数据库中
        int rows = activityContentMapper.updateById(activityContent);
        if (rows!=1){
            return  Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        ActivityContent Content = activityContentMapper.selectById(id);
        Activity activity = activityMapper.selectById(Content.getBelong());
        activity.setTitle(title);
        activity.setContent(content);
        activityMapper.updateById(activity) ;
        RedisUtils.RedisHashCache(stringRedisTemplate,activityContent,
                ACTIVITYCONTENT_PREFIX , id.toString() , ACTIVITY_EXPIRETIME , TimeUnit.MINUTES );
        //返回结果
        return Result.ok(200);
    }

    @Override
    public Result showActivityContent(Integer id) {
        OtherUtils.SearchBySign(ActivityContent.class,id,(sign->activityContentMapper.selectById(sign)));
        //搜索文体活动缓存
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(ACTIVITY_PREFIX + id.toString());
        if (!map.isEmpty()){
            ActivityContent activityContent = BeanUtil.fillBeanWithMap(map, new ActivityContent(), true);
            return Result.ok(200,activityContent) ;
        }
        //搜索文体活动
        ActivityContent activityContent = activityContentMapper.selectOne(new QueryWrapper<ActivityContent>().eq("id", id));
        //查看文体活动是否被删除
        //被删除，报错
        if (activityContent.getIsDelete()==1){
            return Result.fail(403,"该文体活动不存在") ;
        }
        //未被删除，返回结果
        //添加缓存
        RedisUtils.RedisHashCache(stringRedisTemplate,activityContent ,
                ACTIVITYCONTENT_PREFIX , id.toString() , ACTIVITY_EXPIRETIME , TimeUnit.MINUTES);
        return Result.ok(200,activityContent);
    }
}
