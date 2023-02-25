package com.xiaomaotongzhi.huilan.utils;

import com.xiaomaotongzhi.huilan.entity.ActivityContent;
import com.xiaomaotongzhi.huilan.entity.Letter;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.ActivityContentMapper;
import com.xiaomaotongzhi.huilan.mapper.ActivityMapper;
import com.xiaomaotongzhi.huilan.mapper.LetterMapper;
import com.xiaomaotongzhi.huilan.service.UserServiceImpl.ActivityServiceImpl;
import com.xiaomaotongzhi.huilan.utils.exception.ServiceException;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUtils {

    private ActivityContentMapper activityContentMapper ;

    private ActivityMapper activityMapper ;

    private ActivityServiceImpl activityService ;

    private StringRedisTemplate stringRedisTemplate ;

    private LetterMapper letterService ;

    public Integer addActivityContent(String title , Integer uid , String author , String content ,Integer belong) {
        UserVo user = UserHolder.getUser();
        //有，继续执行
        //存储数据
        ActivityContent activityContent = new ActivityContent();
        activityContent.setContent(content);
        activityContent.setUid(uid);
        activityContent.setAuthor(author);
        activityContent.setTitle(title);
        activityContent.setIsDelete(0);
        activityContent.setBelong(belong);
        //添加数据到数据库中
        int rows = activityContentMapper.insert(activityContent);
        if (rows!=1) {
            throw new ServiceException("服务器出现不知名异常") ;
        }

        //返回结果
        return activityContent.getId();
    }

    public Result deleteActivityContent(Integer id) {
        UserVo user = UserHolder.getUser();
        int rows = activityContentMapper.deleteById(id);
        if (rows==0) {
            throw new ServiceException("服务器出现不知名异常") ;
        }
        //返回结果
        return Result.ok(200);
    }

    public Result sendLetter(Integer uid , Letter letter) {
        int rows = letterService.insert(letter);
        if (rows==0){
            return Result.fail(401,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200) ;
    }

}
