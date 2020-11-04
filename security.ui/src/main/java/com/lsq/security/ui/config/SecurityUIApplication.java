
package com.lsq.security.ui.config;

import java.text.ParseException;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.lsq.core.config.CoreConfiguration;
import com.lsq.core.ui.config.SwaggerConfig;
import com.lsq.core.ui.config.TomcatConfiguration;
import com.lsq.security.ui.components.SecurityUIScanMarker;

@Configuration()
@Import({ CoreConfiguration.class, TomcatConfiguration.class, SwaggerConfig.class,
		DispatcherServletAutoConfiguration.class })
@ComponentScan(basePackageClasses = { SecurityUIScanMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
@SpringBootApplication
@PropertySource("classpath:security.ui.properties")
public class SecurityUIApplication {
	public static void main(String[] args) throws ParseException {
		SpringApplication.run(SecurityUIApplication.class, args);
	}

	public static void show(Supplier<String> name) {
		System.out.println(name.get());
	}

}
