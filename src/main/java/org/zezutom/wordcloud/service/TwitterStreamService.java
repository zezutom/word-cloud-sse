package org.zezutom.wordcloud.service;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		
	private Set<String> filters;
	
	@Autowired
	private WordCounter wordCounter;
	
	@Autowired
	private TweetHashtagExtractor hashTagExtractor;
	
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
		emitters = new ArrayList<>();
		filters = new HashSet<>();
		filters.addAll(Arrays.asList("Android", "iPhone", "Nokia"));
		stream = twitter.streamingOperations().filter(filtersToString(), Arrays.asList(this));		
	}
	
	@PreDestroy
	public void stop() {		
		if (stream != null) stream.close();
		emitters.clear();
		emitters = null;
		filters.clear();
		filters = null;
	}
	
	public String filtersToString() {
		StringBuilder sb = new StringBuilder();
		filters.forEach(filter -> sb.append(filter).append(","));
		sb.setLength(Math.max(sb.length() - 1, 0));
		return sb.toString();
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
				String[] hashTags = hashTagExtractor.extract(tweet.getText());
				
				if (hashTags.length > 0) {
					System.out.println("Notifying emitters..");
					List<WordCount> wordCounts = wordCounter.countWords(hashTags);	
					emitter.send(event()
							.reconnectTime(3000)
							.data(wordCounts));
					//emitter.complete();
					System.out.println("Emission completed!");
				}

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
