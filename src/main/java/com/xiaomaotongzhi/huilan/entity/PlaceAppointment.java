package com.xiaomaotongzhi.huilan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("t_placeappointment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceAppointment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    private Integer uid ;
    private Integer pid ;
    private Integer sign ;
    private Integer state;
    private LocalDateTime time ;
}
