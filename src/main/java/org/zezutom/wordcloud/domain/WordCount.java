package org.zezutom.wordcloud.domain;

public class WordCount {

	private String text;
	
	private long weight;

	public WordCount(String text, long weight) {
		super();
		this.text = text;
		this.weight = weight;
	}

	public String getText() {
		return text;
	}

	public long getWeight() {
		return weight;
	}
		
}
