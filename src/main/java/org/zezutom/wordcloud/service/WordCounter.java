package org.zezutom.wordcloud.service;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.zezutom.wordcloud.domain.WordCount;

@Service
public class WordCounter {

	public List<WordCount> countWords(String... words) {
		return Arrays.asList(words)
				.stream()
				.filter(word -> !word.isEmpty())
				.map(word -> word.trim().toLowerCase())
				.collect(groupingBy(Function.identity(), counting()))
				.entrySet().stream()
				.map(entry -> new WordCount(entry.getKey(), entry.getValue()))
				.collect(toList());				
	}
}
