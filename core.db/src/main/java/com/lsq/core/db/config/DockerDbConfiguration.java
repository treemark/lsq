package com.lsq.core.db.config;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.json.JsonString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amihaiemil.docker.Container;
import com.lsq.core.docker.components.mysql.MysqlDockerService;
import com.lsq.core.docker.components.mysql.NoContainerFoundException;

@Configuration()

public class DockerDbConfiguration {

	Logger logger = LoggerFactory.getLogger(DockerDbConfiguration.class);

	/***
	 * The persistent mysql container for the application.
	 * 
	 * @param dockerSrv
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Bean
	Container mysqlContainer(MysqlDockerService dockerSrv)
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		Container container = null;
		JsonString state = null;
		while (state == null || !state.getString().equals("running")) {

			container = getMysqlContainer(dockerSrv);
			state = (JsonString) container.get("State");
			if (state != null && !state.getString().equals("running")) {
				container.start();
				dockerSrv.waitForLog(container, "ready for connections", 1000 * 60);
			}
			Thread.sleep(1000);
		}

		return container;

	}

	private Container getMysqlContainer(MysqlDockerService dockerSrv) throws IOException {
		Container container;
		try {
			container = dockerSrv.findContainer("coreDockerDb");
		} catch (NoContainerFoundException e) {
			container = dockerSrv.createMysqlContainer("coreDockerDb", "mysql/mysql-server:8.0.16-1.1.11", "root1234",
					"coreDb");
		}
		return container;
	}
}
