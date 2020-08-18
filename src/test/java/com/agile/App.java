package com.agile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 入口工程
 *
 * @author tudou
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        try {
            new SpringApplication(App.class).run(args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
