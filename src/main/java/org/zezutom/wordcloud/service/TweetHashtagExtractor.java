package org.zezutom.wordcloud.service;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;

import org.springframework.stereotype.Service;

public class TweetHashtagExtractor {

	public static String[] extract(String tweetText) {
		return Arrays.asList(tweetText.split("\\s"))
				.stream()
				.filter(word -> word.startsWith("#"))
				.map(word -> word.replaceAll("#", ""))
				.collect(toList())
				.toArray(new String[0]);
	}
}
