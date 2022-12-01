package com.xiaomaotongzhi.huilan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_clubcontent")
public class ClubContent implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    private String username ;
    private Integer uid ;
    private String title ;
    private String author ;
    private String content ;
    private Integer liked ;
    private Integer comments ;
    private LocalDateTime time ;
    @TableField(exist = false)
    private Boolean isLiked ;
}
