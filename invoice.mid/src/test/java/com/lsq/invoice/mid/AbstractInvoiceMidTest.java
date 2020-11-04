package com.lsq.invoice.mid;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.lsq.core.config.CoreConfiguration;
import com.lsq.core.db.config.CoreDbConfiguration;
import com.lsq.core.db.config.DockerDbConfiguration;
import com.lsq.core.db.config.JPAConfiguration;
import com.lsq.core.db.config.MysqlIntegrationTestConfiguration;
import com.lsq.core.docker.config.DockerUtilConfiguration;
import com.lsq.invoice.mid.config.InvoiceMidConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest({ "spring.main.allow-bean-definition-overriding=true" })
@ContextConfiguration(classes = { CoreConfiguration.class, CoreDbConfiguration.class, DockerDbConfiguration.class,
		DockerUtilConfiguration.class, JPAConfiguration.class, MysqlIntegrationTestConfiguration.class,
		InvoiceMidConfiguration.class })
public abstract class AbstractInvoiceMidTest {

}
