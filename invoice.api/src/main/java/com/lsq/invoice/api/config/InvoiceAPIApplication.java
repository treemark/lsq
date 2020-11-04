
package com.lsq.invoice.api.config;

import java.text.ParseException;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lsq.core.config.CoreConfiguration;
import com.lsq.core.db.config.CoreDbConfiguration;
import com.lsq.core.db.config.DockerDbConfiguration;
import com.lsq.core.db.config.JPAConfiguration;
import com.lsq.core.docker.config.DockerUtilConfiguration;
import com.lsq.core.rest.config.CoreRestConfiguration;
import com.lsq.core.rest.config.SwaggerConfig;
import com.lsq.invoice.api.components.InvoiceApiMarker;
import com.lsq.invoice.mid.config.InvoiceMidConfiguration;

@Configuration()
@Import({ CoreConfiguration.class, CoreDbConfiguration.class, DockerDbConfiguration.class,
		DockerUtilConfiguration.class, JPAConfiguration.class, CoreRestConfiguration.class,
		InvoiceMidConfiguration.class, SwaggerConfig.class, DispatcherServletAutoConfiguration.class })
@ComponentScan(basePackageClasses = { InvoiceApiMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
//@SpringBootApplication
@EnableWebMvc

public class InvoiceAPIApplication {
	public static void main(String[] args) throws ParseException {
		SpringApplication.run(InvoiceAPIApplication.class, args);
	}

	public static void show(Supplier<String> name) {
		System.out.println(name.get());
	}
}
