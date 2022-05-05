package com.study.totee.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthCheckController {
    // AWS 로드벨런서는 기본 경로 "/"에 HTTP 요청을 보내 애플리케이션 동작 확인
    @GetMapping("/")
    public String healthCheck(){
        return "The service is up and running...";
    }
}
