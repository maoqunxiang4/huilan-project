package com.xiaomaotongzhi.huilan.service.UserServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.Letter;
import com.xiaomaotongzhi.huilan.mapper.LetterMapper;
import com.xiaomaotongzhi.huilan.service.ILetterService;
import com.xiaomaotongzhi.huilan.utils.OtherUtils;
import com.xiaomaotongzhi.huilan.utils.Result;
import com.xiaomaotongzhi.huilan.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LetterServiceImpl implements ILetterService {
    @Autowired
    private LetterMapper letterMapper ;

    @Override
    public Result showLetter() {
        List<Letter> letters = letterMapper
                .selectList(new QueryWrapper<Letter>().eq("dest", UserHolder.getUser().getId()));

        return OtherUtils.showContent(letters,new Letter()) ;
    }

    @Override
    public Result isRead(Integer id) {
        Letter letter = new Letter();
        letter.setIsRead(1);
        int rows = letterMapper.update(letter, new QueryWrapper<Letter>().eq("id", id));
        if (rows==0){
            return Result.ok(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }

    @Override
    public Result deleteLetter(Integer id) {
        int rows = letterMapper.deleteById(id);
        if (rows==0){
            return Result.ok(503,"服务器出现不知名异常，请稍后重试") ;
        }
        return Result.ok(200);
    }
}
