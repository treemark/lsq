package com.lsq.security.mid.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

import com.lsq.security.mid.SecurityMidMarker;

//@SpringBootApplication(scanBasePackages = { "com.lsq.security.mid.components" })
@Configuration
@ConfigurationProperties(prefix = "security.mid")
@PropertySource("classpath:security.mid.properties")
@ComponentScan(basePackageClasses = { SecurityMidMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
public class SecurityMidConfiguration {

}
