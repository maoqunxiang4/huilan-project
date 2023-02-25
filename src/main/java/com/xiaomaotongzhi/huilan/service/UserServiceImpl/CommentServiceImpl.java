package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomaotongzhi.huilan.entity.ClubContent;
import com.xiaomaotongzhi.huilan.entity.Comment;
import com.xiaomaotongzhi.huilan.entity.DoubleComment;
import com.xiaomaotongzhi.huilan.mapper.ClubContentMapper;
import com.xiaomaotongzhi.huilan.mapper.CommentMapper;
import com.xiaomaotongzhi.huilan.mapper.DoubleCommentMapper;
import com.xiaomaotongzhi.huilan.service.ICommentService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import com.xiaomaotongzhi.huilan.utils.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.xiaomaotongzhi.huilan.utils.Constants.DEFAULT_PAGESIZE;

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentMapper commentMapper ;

    @Autowired
    private ClubContentMapper clubContentMapper ;

    @Autowired
    private DoubleCommentMapper doubleCommentMapper ;

    @Override
    public Result addComment(Integer cid , String content) {
        OtherUtils.NotBeNull(cid.toString(),content);
        Comment comment = new Comment();
        UserVo user = UserHolder.getUser();
        comment.setUsername(user.getUsername());
        comment.setUid(user.getId());
        comment.setContent(content);
        comment.setLiked(0);
        comment.setCid(cid);
        comment.setComments(0);
        int rows = commentMapper.insert(comment);
        if (rows==0) {
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        ClubContent clubContent = OtherUtils.SearchBySign
                (ClubContent.class, cid, ((sign) -> clubContentMapper.selectById(sign)));
        clubContent.setComments(clubContent.getComments()+1);
        rows = clubContentMapper.updateById(clubContent);
        if (rows==0) {
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deleteComment(Integer id) {
        Comment comment = commentMapper.selectById(id);
        int rows = commentMapper.deleteById(id);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常");
        }
        ClubContent clubContent = OtherUtils.SearchBySign
                (ClubContent.class,comment.getCid(),((sign)->clubContentMapper.selectById(sign))) ;
        clubContent.setComments(clubContent.getComments()-1);
        rows = clubContentMapper.updateById(clubContent);
        doubleCommentMapper.delete(new QueryWrapper<DoubleComment>().eq("comid",id)) ;
        if (rows==0) {
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showComments(Integer cid ,Integer current) {
        Page<Comment> commentPage = commentMapper.selectPage
                (new Page<Comment>((long) DEFAULT_PAGESIZE * (current - 1), DEFAULT_PAGESIZE), new QueryWrapper<Comment>().eq("cid", cid));
        return OtherUtils.showContent(commentPage.getRecords(),new Comment());
    }
}
