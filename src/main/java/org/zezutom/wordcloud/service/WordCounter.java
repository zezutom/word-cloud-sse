package org.zezutom.wordcloud.service;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.zezutom.wordcloud.domain.WordCount;

@Service
public class WordCounter {

	private Map<String, Long> wordMap = new HashMap<>();
	
	public List<WordCount> countWords(String... words) {
		Arrays.asList(words)
			.stream()				
			.map(word -> word.trim().toLowerCase())
			.forEach(word -> {
				long count = wordMap.containsKey(word) ? wordMap.get(word) + 1 : 1;
				wordMap.put(word, count);
			});
		
		return wordMap.entrySet()
				.stream()
				.map(entry -> new WordCount(entry.getKey(), entry.getValue()))
				.collect(toList());						
	}
}
