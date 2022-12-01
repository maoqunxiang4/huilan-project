package com.xiaomaotongzhi.huilan.utils.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jscode2session {
    private String session_key ;
    private String unionid ;
    private String errmsg ;
    private String openid ;
    private Integer errcode ;
}
