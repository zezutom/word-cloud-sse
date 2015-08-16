package org.zezutom.wordcloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zezutom.wordcloud.domain.WordCount;
import org.zezutom.wordcloud.service.TwitterStreamService;
import org.zezutom.wordcloud.service.WordCounter;

@Controller
public class AppController {

	@Autowired
	private TwitterStreamService streamService;
	
	@Autowired
	private WordCounter wordCounter;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "index";
	}
	
	@RequestMapping(value = "/words", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody List<WordCount> wordCounts() {
		
		// TODO pull words from a news feed
		return wordCounter.countWords(
			"Lorem",
			"Ipsum",
			"Dolor",
			"Sit",
			"Amet",
			"Consectetur",
			"Adipiscing",
			"Elit",
			"Nam et",
			"Leo",
			"Sapien",
			"Pellentesque",
			"habitant",
			"morbi",
			"tristisque",
			"senectus",
			"et netus",
			"et malesuada",
			"fames",
			"ac turpis",
			"egestas",
			"Aenean",
			"vestibulum",
			"elit",
			"sit amet",
			"metus",
			"adipiscing",
			"ut ultrices"		
		);		
	}
	
	@RequestMapping(value = "/words/stream", method = RequestMethod.GET, produces = "text/event-stream")
	public ResponseEntity<SseEmitter> streamWords() {		
		SseEmitter emitter = new SseEmitter();
		streamService.addEmitter(emitter);
		return ResponseEntity.ok(emitter);
	}	
}
