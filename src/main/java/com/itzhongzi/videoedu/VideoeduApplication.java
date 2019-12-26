package com.itzhongzi.videoedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.itzhongzi.videoedu.mapper")
//开启事物管理
@EnableTransactionManagement
public class VideoeduApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoeduApplication.class, args);
	}

}
