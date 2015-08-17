package org.zezutom.wordcloud;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@EnableAsync
@ComponentScan(basePackages = {
		"org.zezutom.wordcloud.controller", 
		"org.zezutom.wordcloud.service"})
@PropertySource({"file:///Users/tomas/twitter.properties"})
public class WebConfig extends WebMvcConfigurerAdapter {

	@Value("${consumer.key}")
	private String consumerKey;
	
	@Value("${consumer.secret}")
	private String consumerSecret;
	
	@Value("${access.token}")
	private String accessToken;
	
	@Value("${access.secret}")
	private String accessSecret;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/app/**").addResourceLocations("/WEB-INF/app/");
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter());
		converters.add(new StringHttpMessageConverter());
	}	
	
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(3000);
	}
	@Bean
	public Twitter twitter() {
		return new TwitterTemplate(
				consumerKey, 
				consumerSecret, 
				accessToken, 
				accessSecret
		);
	} 
		
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/app/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;		
	}
	
	@Bean
	public static CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
		return new CommonAnnotationBeanPostProcessor();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}			
}
