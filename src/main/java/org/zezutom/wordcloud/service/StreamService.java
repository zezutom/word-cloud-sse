package org.zezutom.wordcloud.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.zezutom.wordcloud.domain.FilterTag;
import org.zezutom.wordcloud.domain.WordCount;

@Service
public class StreamService {

	private Logger logger = LoggerFactory.getLogger(StreamService.class);
	
	private BlockingQueue<WordCount> queue = new ArrayBlockingQueue<>(10);
	
	private Map<String, List<Thread>> workerMap = new ConcurrentHashMap<>();
	
	private Set<String> subscriptions = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	private Map<String, List<FilterTag>> filterMap = new ConcurrentHashMap<>();
	
	@Autowired
	private Twitter twitter;
		
	public synchronized String subscribe() {
		String subId = UUID.randomUUID().toString();		
		subscriptions.add(subId);
		return subId;
	}
	
	public boolean unsubscribe(String subId) {
		if (isValid(subId) && workerMap.containsKey(subId)) {
			synchronized(this) {
				workerMap.get(subId).forEach(worker -> worker.interrupt());
				return workerMap.remove(subId) != null && subscriptions.remove(subId);
			}			
		} else {
			return false;
		}		
	}
	
	public void listen(String subId, SseEmitter emitter) {
		if (isValid(subId)) {
			synchronized(this) {
				Producer producer = new Producer(twitter, queue);
				Consumer consumer = new Consumer(emitter, queue);
								
				List<Thread> subscription = Arrays.asList(producer, consumer)
											.stream()
											.map(r -> new Thread(r, "Client: " + subId))
											.collect(Collectors.toList());
				subscription.forEach(t -> t.start());
				workerMap.put(subId, subscription);			
			}			
		}
	}
	
	public List<FilterTag> getFilters(String subId, String query) {
		final String sanitizedQuery = sanitize(query);
		List<FilterTag> filters;
		
		if (isValid(subId) && filterMap.containsKey(subId)) {
			if (query.isEmpty()) {
				filters = filterMap.get(subId); 
			} else {
				filters = filterMap.get(subId)
							.stream()
							.filter(tag -> tag.textContains(sanitizedQuery))
							.collect(Collectors.toList());
			}	
			filters = Collections.unmodifiableList(filters);
		} else {
			filters = Collections.emptyList();
		}		
		return filters;
	}
	
	public List<FilterTag> addFilter(String subId, String text) {
		if (isValid(subId)) {
			synchronized(this) {
				final String sanitizedText = sanitize(text);
				
				List<FilterTag> filters = filterMap.get(subId);
				
				if (!text.isEmpty()) {									
					if (filters == null) {
						filters = new ArrayList<>();
						filterMap.put(subId, filters);
					}
					filters.add(new FilterTag(sanitizedText));					
				}
				return (filters == null) ? Collections.emptyList() : Collections.unmodifiableList(filters);
			}
		} else {
			return Collections.emptyList();
		}
	}
	
	public List<FilterTag> removeFilter(String subId, String text) {
		if (isValid(subId)) {
			synchronized(this) {
				final String sanitizedText = sanitize(text);
				List<FilterTag> filters = filterMap.get(subId);
				List<FilterTag> toRemove = new ArrayList<>();
				if (filters != null) {
					filters.forEach(tag -> {
						if (tag.textEquals(sanitizedText)) {
							toRemove.add(tag);
						}
					});
					filters.removeAll(toRemove);
				}
				return Collections.unmodifiableList(filters);
			}
		} else {
			return Collections.emptyList();
		}
	}
	
	@PreDestroy
	public void stop() {
		workerMap.entrySet().forEach(entry -> {
			entry.getValue().forEach(worker -> worker.interrupt());
		});
	}
	
	private boolean isValid(String subId) {
		boolean valid = subscriptions.contains(subId);
		if (!valid) {
			logger.error("Subscription not found: " + subId);	
		}
		return valid;
	}
	
	private String sanitize(String query) {
		if (query != null) {
			// TODO remove suspicious characters
		} else {
			query = "";
		}
		return query;
	}
}
