package com.xiaomaotongzhi.huilan.utils.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private Integer id ;
    private String username ;
    private Integer isDelete ;
    private Integer attestation ;
    private String token ;
    private String session_key ;
    private String openId ;
}
