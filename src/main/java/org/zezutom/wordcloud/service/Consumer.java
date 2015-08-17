package org.zezutom.wordcloud.service;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zezutom.wordcloud.domain.WordCount;

public class Consumer implements Stoppable {

	private BlockingQueue<WordCount> queue;
	
	private SseEmitter emitter;
		
	private boolean keepRunning;
	
	public Consumer(BlockingQueue<WordCount> queue, SseEmitter emitter) {
		this.queue = queue;
		this.emitter = emitter;
		this.keepRunning = true;
	}

	@Override
	public void run() {
		while(keepRunning && !queue.isEmpty()) {
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
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("error occurred");
			}			
		}
	}

	@Override
	public void stop() {
		keepRunning = false;		
	}

}
