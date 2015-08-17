package org.zezutom.wordcloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.zezutom.wordcloud.domain.WordCount;

public class Producer implements Runnable, StreamListener {
				
	private Logger logger = LoggerFactory.getLogger(Producer.class);
	
	private BlockingQueue<WordCount> queue;
	
	private Stream stream;
	
	public Producer(Twitter twitter, BlockingQueue<WordCount> queue) {
		this.queue = queue;
		stream = twitter.streamingOperations().filter("Android,iPhone,Google,Microsoft,Blackberry", Arrays.asList(this));
	}

	@Override
	public void run() {
		while(!Thread.interrupted()) {
			// Just keep running
		}
		logger.info("Shutting down " + Thread.currentThread().getName());
		if (stream != null) {
			stream.close();
		}
	}
	
	@Override
	public void onTweet(Tweet tweet) {
		String[] hashTags = TweetHashtagExtractor.extract(tweet.getText());
		if (hashTags.length > 0) {
			List<WordCount> wordCounts = WordCounter.getInstance().countWords(hashTags);
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
	
}
