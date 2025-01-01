package org.example.learn.spring.boot.schedule.hello.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 通过@Component将该类注册到spring容器
 */
@Component
public class ExampleTask2 {

    public static final Logger logger = LoggerFactory.getLogger(ExampleTask2.class);

    @Scheduled(cron = "0/3 * * * * *")
    public void doFoo2() {
        logger.info("executing doFoo2");
    }
}
