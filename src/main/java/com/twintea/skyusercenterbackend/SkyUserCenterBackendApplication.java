package com.twintea.skyusercenterbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.twintea.skyusercenterbackend.mapper")
public class SkyUserCenterBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyUserCenterBackendApplication.class, args);
    }

}
