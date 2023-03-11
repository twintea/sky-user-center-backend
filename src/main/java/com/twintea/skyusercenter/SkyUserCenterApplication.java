package com.twintea.skyusercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.twintea.skyusercenter.mapper")
public class SkyUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyUserCenterApplication.class, args);
    }

}
