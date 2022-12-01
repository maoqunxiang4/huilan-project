package com.xiaomaotongzhi.huilan;

import cn.hutool.json.JSONUtil;
import com.xiaomaotongzhi.huilan.entity.Activity;
import com.xiaomaotongzhi.huilan.entity.ActivityContent;
import com.xiaomaotongzhi.huilan.entity.ContentVo;
import com.xiaomaotongzhi.huilan.entity.User;
import com.xiaomaotongzhi.huilan.mapper.ActivityContentMapper;
import com.xiaomaotongzhi.huilan.mapper.UserMapper;
import com.xiaomaotongzhi.huilan.utils.Result;
import net.sf.jsqlparser.schema.Database;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.activation.DataSource;
import java.util.ArrayList;
import java.util.List;

import static com.xiaomaotongzhi.huilan.utils.Constants.ACTIVITY_PREFIX;

@SpringBootTest
class HuilanApplicationTests {

    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private ActivityContentMapper activityContentMapper ;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Test
    void test1(){
        ArrayList<ContentVo> contentVos = userMapper.selectAll(5);
        System.out.println();
        for (ContentVo contentVo : contentVos) {
            System.out.println(contentVo);
        }
        System.out.println();
    }

}
