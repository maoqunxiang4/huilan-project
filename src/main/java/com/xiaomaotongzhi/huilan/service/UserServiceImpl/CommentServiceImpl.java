package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomaotongzhi.huilan.entity.ClubContent;
import com.xiaomaotongzhi.huilan.entity.Comment;
import com.xiaomaotongzhi.huilan.mapper.ClubContentMapper;
import com.xiaomaotongzhi.huilan.mapper.CommentMapper;
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

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentMapper commentMapper ;

    @Autowired
    private ClubContentMapper clubContentMapper ;

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
        int rows = commentMapper.deleteById(id);
        if (rows==0){
            return Result.fail(503,"服务器出现不知名异常");
        }
        Comment comment = commentMapper.selectById(id);
        ClubContent clubContent = OtherUtils.SearchBySign
                (ClubContent.class,comment.getCid(),((sign)->clubContentMapper.selectById(sign))) ;
        clubContent.setComments(clubContent.getComments()-1);
        rows = clubContentMapper.updateById(clubContent);
        if (rows==0) {
            return Result.fail(503,"服务器出现不知名异常") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result showComments(Integer cid) {
        List<Comment> comments = commentMapper.selectList(new QueryWrapper<Comment>().eq("cid",cid));
        return OtherUtils.showContent(comments,new Comment());
    }
}
