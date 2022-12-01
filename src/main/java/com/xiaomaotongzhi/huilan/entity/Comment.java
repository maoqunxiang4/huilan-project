package com.xiaomaotongzhi.huilan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("t_comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    private String username ;
    private Integer uid ;
    private String content ;
    private Integer liked ;
    private Integer comments;
    private Integer cid ;
}
