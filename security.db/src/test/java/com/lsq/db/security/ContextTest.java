package com.lsq.db.security;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import com.lsq.core.config.CoreConfiguration;
import com.lsq.core.db.config.CoreDbConfiguration;
import com.lsq.core.db.config.DockerDbConfiguration;
import com.lsq.core.db.config.JPAConfiguration;
import com.lsq.core.db.config.MysqlIntegrationTestConfiguration;
import com.lsq.core.docker.config.DockerUtilConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest({ "spring.main.allow-bean-definition-overriding=true" })
@ContextConfiguration(classes = { CoreConfiguration.class, CoreDbConfiguration.class, DockerDbConfiguration.class,
		DockerUtilConfiguration.class, JPAConfiguration.class, MysqlIntegrationTestConfiguration.class })
public class ContextTest {

	Logger logger = LoggerFactory.getLogger(ContextTest.class);

	@Autowired
	@Qualifier("coreDataSource")
	DataSource ds;

	@Autowired
	NamedParameterJdbcTemplate jdbcTemp;

	@PersistenceContext
	EntityManager em;

	@Autowired
	TransactionTemplate txTemp;

	public static void main(String[] args) {
		SpringApplication app = null;// = new SpringApplication(App.class);
		app.addInitializers(new CustomAppCtxInitializer());
		ConfigurableApplicationContext ctx = app.run();
	}

	private static class CustomAppCtxInitializer implements ApplicationContextInitializer<GenericApplicationContext> {
		// spring.main.allow-bean-definition-overriding=true
		@Override
		public void initialize(GenericApplicationContext applicationContext) {
			applicationContext.getDefaultListableBeanFactory().setAllowBeanDefinitionOverriding(false);
		}

	}

	@Test
	public void doNothing() {

	}
}
