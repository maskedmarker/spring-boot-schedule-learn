package org.example.learn.spring.boot.schedule.hello.config;

import org.example.learn.spring.boot.schedule.hello.task.ExampleTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 通过在configuration-class上添加@EnableScheduling来开启spring的定时任务功能
 *
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Bean
    public ExampleTask exampleTask() {
        return new ExampleTask();
    }
}
