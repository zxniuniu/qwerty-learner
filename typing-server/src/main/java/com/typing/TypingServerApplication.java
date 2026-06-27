package com.typing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 打字练习独立后端启动类（Spring Boot 2.7）。
 * 直接 mvn spring-boot:run 即可启动，默认内存库 H2，零外部依赖。
 */
@SpringBootApplication
@MapperScan("com.typing.mapper")
public class TypingServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TypingServerApplication.class, args);
  }
}
