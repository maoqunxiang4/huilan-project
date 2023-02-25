package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomaotongzhi.huilan.entity.Comment;
import com.xiaomaotongzhi.huilan.entity.DoubleComment;
import com.xiaomaotongzhi.huilan.mapper.CommentMapper;
import com.xiaomaotongzhi.huilan.mapper.DoubleCommentMapper;
import com.xiaomaotongzhi.huilan.service.IDoubleCommentService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.util.List;

import static com.xiaomaotongzhi.huilan.utils.Constants.DEFAULT_PAGESIZE;

@Service
@Transactional
public class DoubleCommentServiceImpl implements IDoubleCommentService {
    @Autowired
    private DoubleCommentMapper doubleCommentMapper;

    @Autowired
    private CommentMapper commentMapper ;

    @Override
    public Result addComment(Integer comid , String content) {
        OtherUtils.NotBeNull(comid.toString(),content);
        DoubleComment comment = new DoubleComment();
        UserVo user = UserHolder.getUser();
        comment.setUsername(user.getUsername());
        comment.setUid(user.getId());
        comment.setContent(content);
        comment.setLiked(0);
        comment.setComid(comid);
        int rows = doubleCommentMapper.insert(comment);
        if (rows==0) {
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        UpdateWrapper<Comment> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",comid).setSql(" comments = comments +1 ") ;
        commentMapper.update(null,wrapper) ;
        return Result.ok(200);
    }

    @Override
    public Result deleteComment(Integer id) {
        DoubleComment doubleComment = doubleCommentMapper.selectById(id);
        int rows = doubleCommentMapper.deleteById(id);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常");
        }
        Integer comid = doubleComment.getComid();
        UpdateWrapper<Comment> wrapper = new UpdateWrapper<Comment>().eq("id", comid).setSql(" comments = comments - 1 ");
        rows = commentMapper.update(null, wrapper);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常");
        }
        return Result.ok(200);
    }

    @Override
    public Result showComments(Integer comid ,Integer current) {
        QueryWrapper<DoubleComment> wr = new QueryWrapper<DoubleComment>().eq("comid", comid);
        Page<DoubleComment> page = new Page<>((long)DEFAULT_PAGESIZE * (current-1) , DEFAULT_PAGESIZE);
        Page<DoubleComment> doubleCommentPage = doubleCommentMapper.selectPage(page, wr);
        return OtherUtils.showContent(doubleCommentPage.getRecords(),new DoubleComment());
    }
}
