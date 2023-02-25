package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomaotongzhi.huilan.entity.ClubContent;
import com.xiaomaotongzhi.huilan.entity.Comment;
import com.xiaomaotongzhi.huilan.entity.DoubleComment;
import com.xiaomaotongzhi.huilan.mapper.ClubContentMapper;
import com.xiaomaotongzhi.huilan.mapper.CommentMapper;
import com.xiaomaotongzhi.huilan.mapper.DoubleCommentMapper;
import com.xiaomaotongzhi.huilan.service.IClubContentService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import io.swagger.models.auth.In;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.xiaomaotongzhi.huilan.utils.Constants.CLUBCONTENT_PREFIX;
import static com.xiaomaotongzhi.huilan.utils.Constants.DEFAULT_PAGESIZE;

@Service
@Transactional
public class ClubContentServiceImpl implements IClubContentService {
    @Autowired
    private ClubContentMapper clubContentMapper ;

    @Autowired
    private CommentMapper commentMapper ;

    @Autowired
    private DoubleCommentMapper doubleCommentMapper ;

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
        ArrayList<Integer> ids = new ArrayList<>();
        List<Comment> comments = commentMapper.selectList(new QueryWrapper<Comment>().eq("cid", id));
        QueryWrapper<DoubleComment> wrapper = new QueryWrapper<>();
        for (Comment comment : comments) {
            ids.add(comment.getId()) ;
            wrapper.eq("comid",comment.getId()) ;
        }
        doubleCommentMapper.delete(wrapper);
        commentMapper.deleteBatchIds(ids) ;
        int rows = clubContentMapper.deleteById(id);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常") ;
        }

        return Result.ok(200);
    }

    @Override
    public Result showAllClubContent(Integer current) {
        Page<ClubContent> clubContentPage = clubContentMapper.selectPage(new Page<ClubContent>((long) DEFAULT_PAGESIZE * (current - 1), DEFAULT_PAGESIZE)
                , new QueryWrapper<ClubContent>().orderByDesc("time"));
        return OtherUtils.showContent(clubContentPage.getRecords(),new ClubContent()) ;
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
        stringRedisTemplate.opsForSet().add(key,String.valueOf(-1));
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, uid);
        if (BooleanUtil.isFalse(isMember)){
            UpdateWrapper<ClubContent> wrapper = new UpdateWrapper<ClubContent>().eq("id", id).setSql("liked= liked +1");
            int isSuccess = clubContentMapper.update(null, wrapper);
            if (isSuccess==1){
                stringRedisTemplate.opsForSet().add(key,uid) ;
            }
        }else {
            UpdateWrapper<ClubContent> wrapper = new UpdateWrapper<ClubContent>().eq("id", id).setSql("liked=liked -1 ");
            int isSuccess = clubContentMapper.update(null, wrapper);
            if (isSuccess==1){
                stringRedisTemplate.opsForSet().remove(key,uid) ;
            }
        }
        return Result.ok(200,clubContentMapper.selectById(id));
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
