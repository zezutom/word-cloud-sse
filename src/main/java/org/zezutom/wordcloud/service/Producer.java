package org.zezutom.wordcloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.zezutom.wordcloud.domain.WordCount;

public class Producer implements Stoppable, StreamListener {
		
	private final BlockingQueue<WordCount> queue;
			
	private WordCounter wordCounter;
	
	private boolean keepRunning;
	
	public Producer(Twitter twitter, WordCounter wordCounter, BlockingQueue<WordCount> queue) {
		this.wordCounter = wordCounter;
		this.queue = queue;
		this.keepRunning = true;
		twitter.streamingOperations().filter("Android", Arrays.asList(this));
	}

	@Override
	public void run() {
		while(keepRunning) {
			// Just keep running
		}			
	}

	@Override
	public void onTweet(Tweet tweet) {
		System.out.println("new tweet: " + tweet.getText());
		String[] hashTags = TweetHashtagExtractor.extract(tweet.getText());
		if (hashTags.length > 0) {
			List<WordCount> wordCounts = wordCounter.countWords(hashTags);
			queue.addAll(wordCounts);
		}
	}

	@Override
	public void onDelete(StreamDeleteEvent deleteEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLimit(int numberOfLimitedTweets) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWarning(StreamWarningEvent warningEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		keepRunning = false;
		
	}

}
