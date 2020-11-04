package com.lsq.invoice.mid;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lsq.core.db.components.mysql.util.ShowTableStatusBean;
import com.lsq.invoice.db.entities.Invoice;

//@RunWith(SpringRunner.class)
//@SpringBootTest({ "spring.main.allow-bean-definition-overriding=true" })
//@ContextConfiguration(classes = { CoreConfiguration.class, CoreDbConfiguration.class, DockerDbConfiguration.class,
//		DockerUtilConfiguration.class, JPAConfiguration.class, MysqlIntegrationTestConfiguration.class })
public class TestInvoiceCRUD extends AbstractInvoiceMidTest {

	Logger logger = LoggerFactory.getLogger(TestInvoiceCRUD.class);

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
	public void testInvoicePersist() {

		TransactionCallback<Invoice> createInvoiceCB = (TransactionStatus txstat) -> {
			Invoice test = new Invoice();
			test.setInvoiceAmount(new BigDecimal("1.00"));
			test.setInvoiceDate(new Timestamp(1604443674000l));
			test.setInvoiceId("FroopyDoodle");
			test.setPaymentAmount(new BigDecimal("0.10"));
			test.setPaymentDate(new Timestamp(1604443674000l));
			test.setSupplierId("SnargleFraz");
			test.setTerms(5);
			em.persist(test);
			return test;
		};
		Invoice testInvoice = txTemp.execute(createInvoiceCB);

		TransactionCallback<List<Invoice>> listUsersCB = (TransactionStatus txstat) -> {
			Query users = this.em.createQuery("select i from Invoice i order by i.id");
			return users.getResultList();
		};
		List<Invoice> invoices = txTemp.execute(listUsersCB);
		Assert.assertEquals(1, invoices.size());
		Assert.assertEquals(testInvoice.getInvoiceAmount(), invoices.get(0).getInvoiceAmount());
		Assert.assertEquals(testInvoice.getInvoiceDate().getTime(), invoices.get(0).getInvoiceDate().getTime());
		Assert.assertEquals(testInvoice.getTerms(), invoices.get(0).getTerms());

	}

	@AfterClass
	public static void cleanup() {

	}
}
