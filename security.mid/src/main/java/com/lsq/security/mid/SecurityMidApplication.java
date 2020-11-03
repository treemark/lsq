
package com.lsq.security.mid;

import java.text.ParseException;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.lsq.core.config.CoreConfiguration;
import com.lsq.core.db.config.CoreDbConfiguration;
import com.lsq.core.db.config.DockerDbConfiguration;
import com.lsq.core.db.config.JPAConfiguration;
import com.lsq.core.docker.config.DockerUtilConfiguration;
import com.lsq.core.rest.config.CoreRestConfiguration;
import com.lsq.core.rest.config.SwaggerConfig;
import com.lsq.security.mid.config.SecurityMidConfiguration;

@Configuration()
@Import({ CoreConfiguration.class, CoreDbConfiguration.class, DockerDbConfiguration.class,
		DockerUtilConfiguration.class, JPAConfiguration.class, CoreRestConfiguration.class,
		SecurityMidConfiguration.class, SwaggerConfig.class })

@ComponentScan("com.lsq.security.mid.components")
//@SpringBootApplication
public class SecurityMidApplication {
	public static void main(String[] args) throws ParseException {
		System.getProperties().setProperty("core.rest.http.port", "8081");
		SpringApplication.run(SecurityMidApplication.class, args);
	}

	public static void show(Supplier<String> name) {
		System.out.println(name.get());
	}

}
