package com.lsq.db.security;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lsq.core.config.CoreConfiguration;
import com.lsq.core.db.components.mysql.util.ShowTableStatusBean;
import com.lsq.core.db.config.CoreDbConfiguration;
import com.lsq.core.db.config.DockerDbConfiguration;
import com.lsq.core.db.config.JPAConfiguration;
import com.lsq.core.db.config.MysqlIntegrationTestConfiguration;
import com.lsq.core.docker.config.DockerUtilConfiguration;
import com.lsq.security.db.entities.User;

@RunWith(SpringRunner.class)
@SpringBootTest({ "spring.main.allow-bean-definition-overriding=true" })
@ContextConfiguration(classes = { CoreConfiguration.class, CoreDbConfiguration.class, DockerDbConfiguration.class,
		DockerUtilConfiguration.class, JPAConfiguration.class, MysqlIntegrationTestConfiguration.class })
public class MysqlIntegrationTest {

	Logger logger = LoggerFactory.getLogger(MysqlIntegrationTest.class);

	@Autowired
	@Qualifier("coreDataSource")
	DataSource ds;

	@Autowired
	NamedParameterJdbcTemplate jdbcTemp;

	@PersistenceContext
	EntityManager em;

	@Autowired
	TransactionTemplate txTemp;

	@Test
	public void testJdbcQuery() {

		BeanPropertyRowMapper<ShowTableStatusBean> mapper = new BeanPropertyRowMapper<ShowTableStatusBean>(
				ShowTableStatusBean.class);
		List<ShowTableStatusBean> beans = jdbcTemp.query("show table status", mapper);
		logger.info(beans.toString());

	}

	@Test
	public void testHibernate() {

		TransactionCallback<User> createUserCB = (TransactionStatus txstat) -> {
			User test = new User();
			test.setEmail("mail@somewhere");
			test.setCreateDate(new Date());
			test.setName("some guy");
			test.setActive(true);
			em.persist(test);
			return test;
		};
		User testUser = txTemp.execute(createUserCB);

		TransactionCallback<List<User>> listUsersCB = (TransactionStatus txstat) -> {
			Query users = this.em.createQuery("select u from User u order by u.id");
			return users.getResultList();
		};
		List<User> users = txTemp.execute(listUsersCB);
		Assert.assertEquals(3, users.size());
		Assert.assertEquals(testUser.getName(), users.get(2).getName());
	}

	@AfterClass
	public static void cleanup() {

	}
}
