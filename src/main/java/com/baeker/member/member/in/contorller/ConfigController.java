package com.baeker.member.member.in.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Config Server 와 연결 상태를 확인하는 Controller 입니다.
 */
@RestController
@Slf4j
public class ConfigController {

    @Value("${custom.con}")
    private String con;

    @GetMapping("/test")
    public String conTest() {
        if (!con.isEmpty())
            log.info("Config Server 연결 성공 msg = {}", con);
        return con;
    }

}
