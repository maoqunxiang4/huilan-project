package com.xiaomaotongzhi.huilan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate geteRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return  restTemplate;
    }

}
