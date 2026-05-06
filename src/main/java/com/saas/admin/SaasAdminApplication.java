package com.saas.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.saas.admin.mapper")
public class SaasAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaasAdminApplication.class, args);
    }
}
