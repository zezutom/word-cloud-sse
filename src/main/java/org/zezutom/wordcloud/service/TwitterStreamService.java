package org.zezutom.wordcloud.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zezutom.wordcloud.domain.WordCount;

@Service
public class TwitterStreamService implements StreamListener {
	
	private Twitter twitter;
	
	private Stream stream;
	
	private List<SseEmitter> emitters;
	
	@Autowired
	private WordCounter wordCounter;
	
	@Autowired
	public TwitterStreamService(Twitter twitter) {
		this.twitter = twitter;
	}
	
	public void addEmitter(SseEmitter emitter) {
		if (emitter != null) emitters.add(emitter);
	}
	
	// Monitors Twitter's status updates
	@PostConstruct
	public void start() {
		stream = twitter.streamingOperations().sample(Arrays.asList(this));
		emitters = new ArrayList<>();
	}
	
	@PreDestroy
	public void stop() {		
		if (stream != null) stream.close();
		emitters.clear();
		emitters = null;
	}
	
	@Override
	public void onDelete(StreamDeleteEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLimit(int limit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTweet(Tweet tweet) {
		System.out.println("tweet: " + tweet.getText());
		emitters.forEach((emitter) -> {
			try {
				System.out.println("Notifying emitters..");
				List<WordCount> wordCounts = wordCounter.countWords(tweet.getText().split("\\s"));
				emitter.send(wordCounts);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});				
	}

	@Override
	public void onWarning(StreamWarningEvent e) {
		// TODO Auto-generated method stub
		
	}
}
