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

@TableName("t_activitycontent")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityContent implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id ;
    private Integer uid ;
    private String title ;
    private Integer belong ;
    private String author ;
    private String content ;
    @TableField("isDelete")
    private Integer isDelete ;
}
