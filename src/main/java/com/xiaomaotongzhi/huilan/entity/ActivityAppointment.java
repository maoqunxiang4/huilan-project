package com.xiaomaotongzhi.huilan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("t_activityappointment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAppointment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    private Integer uid ;
    private Integer aid ;
    private Integer sign ;
}
