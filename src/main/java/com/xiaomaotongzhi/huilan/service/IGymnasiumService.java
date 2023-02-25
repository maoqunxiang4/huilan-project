package com.xiaomaotongzhi.huilan.service;


import com.xiaomaotongzhi.huilan.utils.Result;

public interface IGymnasiumService {
    Result addGymnasium( String name) ;
    Result updateGymnasium(Integer id , String name) ;
    Result deleteGymnasium(Integer id) ;
    Result showGymnasium(Integer current) ;
}
