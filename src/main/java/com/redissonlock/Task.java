package com.redissonlock;

import java.util.concurrent.locks.Lock;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Task {

	private static Logger LOG = LoggerFactory.getLogger(Task.class);

	@Autowired
	private RedissonClient redissonClient;

	/*
	 * This task just start a critical section locked by 
	 * Redisson Distributed Lock.
	 * While this task is idle, run this on redis:
	 * CLIENT PAUSE 5000
	 * The above pause will cause a RedisResponseTimeoutException
	 */
	@Scheduled(fixedDelay = 1000)
	public void run() throws InterruptedException {
		Lock lock = redissonClient.getLock("LOCK_TEST");
		LOG.debug("LOCKING...");
		lock.lock();
		LOG.debug("LOCKED!");
		try {
			LOG.info("TASK RUN...");
			Thread.sleep(2000);
		} finally {
			LOG.debug("UNLOCKING...");
			lock.unlock();
			LOG.debug("UNLOCKED!");
		}
	}

	/*
	 *  This task is just to force the use of different threads on above task.
	 *  Locks with the same thread can execute the task even after timeout
	 *  due to reentrant lock feature.
	 */
	@Scheduled(fixedDelay = 1000)
	public void run2() throws InterruptedException {
		Thread.sleep(1000);
	}

}
