package com.lsq.security.mid;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.lsq.core.config.CoreConfiguration;
import com.lsq.core.db.config.CoreDbConfiguration;
import com.lsq.core.db.config.DockerDbConfiguration;
import com.lsq.core.db.config.JPAConfiguration;
import com.lsq.core.docker.config.DockerUtilConfiguration;
import com.lsq.core.rest.config.CoreRestConfiguration;

@Configuration()
@Import({ CoreConfiguration.class, CoreDbConfiguration.class, DockerDbConfiguration.class,
		DockerUtilConfiguration.class, JPAConfiguration.class, CoreRestConfiguration.class })
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
