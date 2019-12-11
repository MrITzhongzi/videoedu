package com.itzhongzi.videoedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itzhongzi.videoedu.mapper")
public class VideoeduApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoeduApplication.class, args);
	}

}
