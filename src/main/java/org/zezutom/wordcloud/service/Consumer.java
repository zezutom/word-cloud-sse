package org.zezutom.wordcloud.service;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zezutom.wordcloud.domain.WordCount;

public class Consumer implements Runnable {

	private Logger logger = LoggerFactory.getLogger(Producer.class);
	
	private BlockingQueue<WordCount> queue;
	
	private SseEmitter emitter;
	
	public Consumer(SseEmitter emitter, BlockingQueue<WordCount> queue) {		
		this.emitter = emitter;
		this.queue = queue;
	}

	@Override
	public void run() {
		while(!Thread.interrupted() && !queue.isEmpty()) {
			try {
				int i = 0;
				List<WordCount> wordCounts = new ArrayList<>();
				while (i <= 20 && !queue.isEmpty()) {
					wordCounts.add(queue.take());
				} 
				emitter.send(event()
						.reconnectTime(3000)
						.data(wordCounts));
			} catch (IOException | InterruptedException e) {
				// Simply abort execution
				break;
			}			
		}
		// Close the connection once done
		logger.info("Shutting down " + Thread.currentThread().getName());
		if (emitter != null) emitter.complete();
	}
}
