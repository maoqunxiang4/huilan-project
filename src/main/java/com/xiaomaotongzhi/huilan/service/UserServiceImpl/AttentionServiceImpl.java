package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import cn.hutool.core.util.BooleanUtil;
import com.xiaomaotongzhi.huilan.config.InterceptorConfig;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.UserMapper;
import com.xiaomaotongzhi.huilan.service.IAttentionService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.xiaomaotongzhi.huilan.utils.Constants.ATTENTION_PREFIX;
import static com.xiaomaotongzhi.huilan.utils.Constants.DEFAULT_PAGESIZE;

@Service
@Transactional
public class AttentionServiceImpl implements IAttentionService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Autowired
    private UserMapper userMapper ;

    @Override
    public Result addOrDeleteAttention(Integer uid) {
        UserVo user = UserHolder.getUser();
        Boolean member = stringRedisTemplate.opsForSet().isMember(ATTENTION_PREFIX + user.getId(), uid.toString());
        if (BooleanUtil.isTrue(member)){
            Long remove = stringRedisTemplate.opsForSet().remove(ATTENTION_PREFIX + user.getId(), uid.toString());
            if (remove==null) return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
            if (remove==0) return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }else {
            Long remove = stringRedisTemplate.opsForSet().add(ATTENTION_PREFIX + user.getId(), uid.toString());
            if (remove==null) return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
            if (remove==0) return Result.fail(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showAttentionList(Integer current) {
        ArrayList<Integer> ids = new ArrayList<>();
        Set<String> members = stringRedisTemplate.opsForSet().members(ATTENTION_PREFIX + UserHolder.getUser().getId());
        if (members==null){
            ArrayList<User> list = new ArrayList<>();
            return Result.ok(200,list) ;
        }
        int skip = (current-1)*DEFAULT_PAGESIZE ;
        int count = 1 ;
        for (String member : members) {
            ids.add(Integer.parseInt(member)) ;
            count++ ;
            if (count>DEFAULT_PAGESIZE) break;
        }
        Iterator<Integer> iterator = ids.iterator();
        ArrayList<User> users = new ArrayList<>();
        while(iterator.hasNext()){
            Integer id = iterator.next();
            User user = userMapper.selectById(id);
            user = isAttention(user);
            users.add(user) ;
        }
        return OtherUtils.showContent(users,new User());
    }

    @Override
    public User isAttention(User user) {
        Boolean isAttention = stringRedisTemplate.opsForSet()
                .isMember(ATTENTION_PREFIX + UserHolder.getUser().getId()
                        , user.getId().toString());
        user.setIsAttention(isAttention);
        return user ;
    }
}
