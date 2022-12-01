package com.xiaomaotongzhi.huilan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("t_doublecomment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoubleComment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    private String username ;
    private Integer uid ;
    private String content ;
    private Integer liked ;
    private Integer comid ;
}
