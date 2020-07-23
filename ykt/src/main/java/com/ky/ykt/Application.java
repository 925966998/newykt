package com.ky.ykt;

import com.ky.ykt.controller.SocketServer;
import com.ky.ykt.controller.SocketServerD;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = {"com.ky.ykt.mapper"})
@Configuration
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {

        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(Application.class, args);
//        SocketServerD server = new SocketServerD();
//        server.startSocketServer(8088);
    }

}

