package com.lsq.core.config;

import java.util.Arrays;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import com.lsq.core.components.CoreScanMarker;

/**
 * Configuration for 'core' middle tier services. Thread pools, performance
 * monitoring, logging services, etc.
 * 
 * @author treemark
 *
 */
@Configuration()
@ComponentScan(basePackageClasses = { CoreScanMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
public class CoreConfiguration {

	Logger logger = LoggerFactory.getLogger(CoreConfiguration.class);

	public static void main(String[] args) {
		SpringApplication.run(CoreConfiguration.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			logger.info("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				logger.info(beanName);
			}
			logger.info("Done Inspecting beans:");
		};
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
//		Resource[] resources = new ClassPathResource[] { new ClassPathResource("foo.properties") };
//		ppc.setLocations(resources);
		ppc.setIgnoreUnresolvablePlaceholders(true);
		return ppc;
	}

	@Bean
	public HttpClient httpClient() {
		return HttpClients.createDefault();
	}

	@Bean
	ConcurrentTaskExecutor taskExecutor() {
		ConcurrentTaskExecutor t = new ConcurrentTaskExecutor(Executors.newFixedThreadPool(10));
		t.setTaskDecorator(new TaskDecorator() {
			@Override
			public Runnable decorate(Runnable runnable) {
				return () -> {
					try {
						runnable.run();
					} catch (Throwable t) {
						logger.error("Task threw exception", t);
					}
				};
			}
		});
		return t;
	}

}