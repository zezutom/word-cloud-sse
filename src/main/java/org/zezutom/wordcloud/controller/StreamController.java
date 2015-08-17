package org.zezutom.wordcloud.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zezutom.wordcloud.domain.FilterTag;
import org.zezutom.wordcloud.service.StreamService;

@Controller
@RequestMapping("/api/v1/stream")
public class StreamController {
	
	@Autowired
	private StreamService service;
	
	@RequestMapping(value = "/subscribe", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String subscribe() {
		return service.subscribe();
	}
	
	@RequestMapping(value = "/unsubscribe/{subId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Boolean unsubscribe(@PathVariable("subId") String subId) {
		return service.unsubscribe(subId);
	}	
	
	@RequestMapping(value = "/words/{subId}", method = RequestMethod.GET, produces = "text/event-stream")
	public ResponseEntity<SseEmitter> streamWords(@PathVariable("subId") String subId) {		
		SseEmitter emitter = new SseEmitter();
		service.listen(subId, emitter);
		return ResponseEntity.ok(emitter);
	}	
	
	@RequestMapping(value = "/filters/{subId}/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<FilterTag> queryFilters(
			@PathVariable("subId") String subId, 
			@PathVariable("query") String query) {
		return service.getFilters(subId, query);
	}	
	
	@RequestMapping(value = "/filters/{subId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<FilterTag> getFilters(@PathVariable("subId") String subId) {
		return service.getFilters(subId, null);
	}
	
	@RequestMapping(value = "/filters/add/{subId}/{text}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FilterTag>> addFilter(
			@PathVariable("subId") String subId, 
			@PathVariable("text") String text) {
		return ResponseEntity
				.created(URI.create("/filters/" + text))
				.body(service.addFilter(subId, text));
	}
	
	@RequestMapping(value = "/filters/remove/{subId}/{text}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<FilterTag> removeFilter(
			@PathVariable("subId") String subId,
			@PathVariable("filter") String text) {
		return service.removeFilter(subId, text);
	}	
}
