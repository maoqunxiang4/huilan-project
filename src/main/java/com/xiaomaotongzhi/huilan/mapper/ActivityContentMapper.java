package com.xiaomaotongzhi.huilan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.xiaomaotongzhi.huilan.entity.ActivityContent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityContentMapper extends BaseMapper<ActivityContent> {
}
