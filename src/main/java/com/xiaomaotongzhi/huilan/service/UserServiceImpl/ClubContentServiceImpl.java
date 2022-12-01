package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaomaotongzhi.huilan.entity.ClubContent;
import com.xiaomaotongzhi.huilan.mapper.ClubContentMapper;
import com.xiaomaotongzhi.huilan.service.IClubContentService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import io.swagger.models.auth.In;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.xiaomaotongzhi.huilan.utils.Constants.CLUBCONTENT_PREFIX;

@Service
@Transactional
public class ClubContentServiceImpl implements IClubContentService {
    @Autowired
    private ClubContentMapper clubContentMapper ;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Override
    public Result addClubContent(String title ,String content) {
        ClubContent clubContent = new ClubContent();
        UserVo user = UserHolder.getUser();
        clubContent.setUid(user.getId());
        clubContent.setTitle(title);
        clubContent.setUsername(user.getUsername());
        clubContent.setAuthor(user.getUsername());
        clubContent.setContent(content);
        clubContent.setLiked(0);
        clubContent.setComments(0);
        LocalDateTime time = LocalDateTime.now();
        clubContent.setTime(time);
        int rows = clubContentMapper.insert(clubContent);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deleteClubContent(Integer id) {
        int rows = clubContentMapper.deleteById(id);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showAllClubContent() {
        List<ClubContent> clubContents = clubContentMapper.selectList(new QueryWrapper<ClubContent>().orderByDesc("time"));
        return OtherUtils.showContent(clubContents,new ClubContent()) ;
    }

    @Override
    public Result showSpecificClubContent(Integer id) {
        OtherUtils.NotBeNull(id.toString());
        return isLiked(id);
    }

    @Override
    public Result updateClubContent(Integer id ,String title ,String content) {
        ClubContent clubContent = clubContentMapper.selectById(id);
        clubContent.setTitle(title);
        clubContent.setContent(content);

        int rows = clubContentMapper.update(clubContent,new QueryWrapper<ClubContent>().eq("id",id));
        if (rows == 0) {
            return Result.fail(503, "服务器出现不知名异常");
        }
        return Result.ok(200) ;
    }

    @Override
    public Result changeLike(Integer id) {
        OtherUtils.NotBeNull(id.toString());
        String key = CLUBCONTENT_PREFIX + id ;
        String uid = UserHolder.getUser().getId().toString() ;
        Map<Object, Boolean> member =
                stringRedisTemplate.opsForSet().isMember(key);
        if (member==null){
            UpdateWrapper<ClubContent> wrapper = new UpdateWrapper<ClubContent>().eq("id", id).setSql("comments=comments+1");
            int isSuccess = clubContentMapper.update(null, wrapper);
            if (isSuccess==1){
                stringRedisTemplate.opsForSet().add(key,uid) ;
            }
        }else {
            UpdateWrapper<ClubContent> wrapper = new UpdateWrapper<ClubContent>().eq("id", id).setSql("comments=comments -1 ");
            int isSuccess = clubContentMapper.update(null, wrapper);
            if (isSuccess==1){
                stringRedisTemplate.opsForSet().remove(key,uid) ;
            }
        }
        return Result.ok(200);
    }

    @Override
    public Result isLiked(Integer id) {
        OtherUtils.NotBeNull(id.toString());
        Boolean member = stringRedisTemplate.opsForSet()
                .isMember(CLUBCONTENT_PREFIX + id, UserHolder.getUser().getId().toString());
        ClubContent clubContent = clubContentMapper.selectById(id);
        if (!BooleanUtil.isTrue(member)){
            clubContent.setIsLiked(false);
            return Result.ok(200,clubContent) ;
        }else {
            clubContent.setIsLiked(true);
            return Result.ok(200,clubContent);
        }
    }
}
