package com.lsq.core.db.config;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.UnexpectedResponseException;
import com.lsq.core.components.util.json.JsonUtil;
import com.lsq.core.db.components.CoreDbMarker;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@PropertySource("classpath:core.db.properties")
@Configuration()
@ComponentScan(basePackageClasses = { CoreDbMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
public class CoreDbConfiguration {

	Logger logger = LoggerFactory.getLogger(CoreDbConfiguration.class);

	public static void main(String[] args) {

	}

	@Value("${core.db.jdbc.password}")
	String pass;

	@Value("${core.db.jdbc.username}")
	String user;

	@Bean
	@Qualifier("coreDataSource")
	public DataSource coreDataSource(Container mysqlContainer) throws UnexpectedResponseException, IOException,
			InterruptedException, ExecutionException, TimeoutException {

		JsonObject inspect = mysqlContainer.inspect();
		String pretty = JsonUtil.prettyPrint(inspect);
		logger.info(pretty);

		JsonValue network = inspect.get("NetworkSettings");//
		JsonString port = (JsonString) network.asJsonObject().get("Ports").asJsonObject().get("3306/tcp").asJsonArray()
				.get(0).asJsonObject().get("HostPort");
		JsonString ip = network.asJsonObject().getJsonString("IPAddress");

		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://%1s:%2s/coreDb?serverTimezone=UTC";
		url = String.format(url, "127.0.0.1", port.getString());
		logger.info("Database URL: " + url);
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(user);
		dataSourceBuilder.password(pass);
		DataSource dataSource = dataSourceBuilder.build();
		// Automatically add a query listener for logging, performance and debugging
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

}
