package com.redissonlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableScheduling
@SpringBootApplication
public class RedissonlockApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedissonlockApplication.class, args);
	}

	@Bean
	@Primary
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("scheduler-");
		scheduler.setPoolSize(10);
		return scheduler;
	}

}
