package com.xiaomaotongzhi.huilan;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("com.xiaomaotongzhi.huilan.mapper")
@EnableOpenApi
public class HuilanApplication {
    public static void main(String[] args) {
        SpringApplication.run(HuilanApplication.class, args);
    }

}
