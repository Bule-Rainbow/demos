package com.sinnet.guacamole.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * You need only the courage to follow your heart.
 *
 * @author Li
 * @date 2020/6/17 18:24
 */
@SpringBootApplication
@ServletComponentScan(basePackages = "com.sinnet.guacamole.demo")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
