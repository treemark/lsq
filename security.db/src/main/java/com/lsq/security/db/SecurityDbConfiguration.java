package com.lsq.security.db;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Configuration
//SpringBootApplication(scanBasePackages = { "com.lsq.security.db" })
@ConfigurationProperties(prefix = "security.db")
@PropertySource("classpath:security.db.properties")
@ComponentScan(basePackageClasses = { SecurityDbMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
public class SecurityDbConfiguration {

	String url;
	String username;
	String password;

	public static void main(String[] args) {
		SpringApplication.run(SecurityDbConfiguration.class, args);
	}

	@Bean
	@Qualifier("securityDataSource")
	public DataSource securityDataSource() {

		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(username);
		dataSourceBuilder.password(password);
		DataSource dataSource = dataSourceBuilder.build();
		// Allows hooks into the logging framework.
		// Put code here to monitor performance and invocation counts of JDBC queries
		QueryExecutionListener listener = new QueryExecutionListener() {
			@Override
			public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {

			}

			@Override
			public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {

			}
		};
		ProxyDataSource proxyed = ProxyDataSourceBuilder.create(dataSource).listener(listener).build();
		return proxyed;
	}

	@Bean
	public NamedParameterJdbcTemplate jdbcTemplate(@Qualifier("securityDataSource") DataSource ds) {
		return new NamedParameterJdbcTemplate(ds);
	}

	@DependsOn("flyWay")
	@Bean()
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setPackagesToScan("com.lsq");

		Properties jpaProperties = new Properties();

		// Configures the used database dialect. This allows Hibernate to create SQL
		// that is optimized for the used database.
		jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));

		// Specifies the action that is invoked to the database when the Hibernate
		// SessionFactory is created or closed.
		jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));

		// Configures the naming strategy that is used when Hibernate creates
		// new database objects and schema elements
		jpaProperties.put("hibernate.ejb.naming_strategy", env.getRequiredProperty("hibernate.ejb.naming_strategy"));

		// If the value of this property is true, Hibernate writes all SQL
		// statements to the console.
		jpaProperties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));

		// If the value of this property is true, Hibernate will format the SQL
		// that is written to the console.
		jpaProperties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));

		entityManagerFactoryBean.setJpaProperties(jpaProperties);

		return entityManagerFactoryBean;
	}

	@Bean
	@Qualifier("flyWay")

	public Flyway flyWay(@Qualifier("securityDataSource") DataSource ds) {
		Flyway flyway = Flyway.configure().dataSource(ds).load();
		// flyway.baseline();
		flyway.migrate();
		return flyway;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
