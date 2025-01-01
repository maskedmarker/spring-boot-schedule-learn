package org.example.learn.spring.boot.schedule.hello.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 通过bean-method将该类注册到spring容器
 */
public class ExampleTask {

    public static final Logger logger = LoggerFactory.getLogger(ExampleTask.class);

    /**
     * The annotated method must expect no arguments.
     * It will typically have a void return type; if not, the returned value will be ignored when called through the scheduler.
     *
     * The fields read from left to right are interpreted as follows.
     * second/minute/hour/day of month/month/day of week
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void doFoo() {
        logger.info("executing doFoo");
    }
}
