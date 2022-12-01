package com.xiaomaotongzhi.huilan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("t_letter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Letter implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    private Integer dest ;
    private String title ;
    private LocalDateTime time ;
    private String Content ;
    @TableField("is_read")
    private Integer isRead ;
}
