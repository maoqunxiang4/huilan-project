package com.xiaomaotongzhi.huilan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaomaotongzhi.huilan.entity.ContentVo;
import com.xiaomaotongzhi.huilan.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {
    ArrayList<ContentVo> selectAll(Integer id) ;
}
