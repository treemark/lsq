package com.lsq.invoice.mid.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

import com.lsq.invoice.mid.components.InvoiceMidMarker;

//@SpringBootApplication(scanBasePackages = { "com.lsq.security.mid.components" })
@Configuration
@ConfigurationProperties(prefix = "invoice.mid")
@PropertySource("classpath:invoice.mid.properties")
@ComponentScan(basePackageClasses = { InvoiceMidMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
public class InvoiceMidConfiguration {

}
