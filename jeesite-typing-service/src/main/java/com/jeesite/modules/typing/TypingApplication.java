package com.jeesite.modules.typing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 打字练习微服务启动类。
 *
 * 说明：接入真实 JeeSite Cloud 工程时，扫描包、自动配置等通常由
 * JeeSite 的启动基类或公共自动配置接管，这里给出标准 Spring Boot + 服务发现入口。
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = { "com.jeesite" })
public class TypingApplication {

  public static void main(String[] args) {
    SpringApplication.run(TypingApplication.class, args);
  }
}
