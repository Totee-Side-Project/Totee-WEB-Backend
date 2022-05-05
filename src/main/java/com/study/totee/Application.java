package com.study.totee;

import com.study.totee.config.properties.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@ComponentScan({ "com.study.totee.*"})
@EnableConfigurationProperties(AppProperties.class)
@EnableWebSecurity
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    } // 내장 WAS 실행, 톰캣 설치필요 x
}