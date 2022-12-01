package com.xiaomaotongzhi.huilan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentVo implements Serializable {
    private String title ;
    private String author ;
    private String content ;
}
