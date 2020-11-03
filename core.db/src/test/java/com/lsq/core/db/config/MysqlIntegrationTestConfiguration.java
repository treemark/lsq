package com.lsq.core.db.config;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.json.JsonString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.amihaiemil.docker.Container;
import com.lsq.core.config.CoreConfiguration;
import com.lsq.core.docker.components.mysql.MysqlDockerTestHarness;
import com.lsq.core.docker.components.mysql.NoContainerFoundException;

/**
 * Used to override the default mysqlContainer bean with a throw away
 * intergration test mysql docker container.
 * 
 * Your test must add
 * 
 * @SpringBootTest({"spring.main.allow-bean-definition-overriding=true"})
 * 
 * To its class definition
 * 
 * 
 * @author treemark
 *
 */
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
//		DataSourceTransactionManagerAutoConfiguration.class, FlywayAutoConfiguration.class,
//		HibernateJpaAutoConfiguration.class }) //
@Configuration
@PropertySource("classpath:docktest.properties")
@Import(CoreConfiguration.class)
public class MysqlIntegrationTestConfiguration {

	Logger logger = LoggerFactory.getLogger(MysqlIntegrationTestConfiguration.class);

	String testMysqlContainerName = "IntegrationTest1";

	@Value("${core.db.jdbc.password}")
	String pass;

	@Value("${core.db.jdbc.dbName}")
	String dbName;

	@Bean
	Container mysqlContainer(MysqlDockerTestHarness dockerSrv)
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		try {
			Container existing = dockerSrv.findContainer(testMysqlContainerName);
			logger.warn("Container already exists. deleting");
			JsonString str = (JsonString) existing.get("State");
			if (str.getString().equals("running"))
				existing.stop();
			existing.remove();
		} catch (NoContainerFoundException e) {

		}
		Container container = dockerSrv.createMysqlContainer(testMysqlContainerName, "mysql/mysql-server:8.0.16-1.1.11",
				pass, dbName);
		container.start();
		dockerSrv.waitForLog(container, "ready for connections", 1000 * 60);
		Thread.sleep(10000);
		return container;
	}

}
