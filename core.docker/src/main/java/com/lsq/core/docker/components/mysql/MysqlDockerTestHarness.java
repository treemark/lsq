package com.lsq.core.docker.components.mysql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.sql.DataSource;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Service;

import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.Docker;
import com.amihaiemil.docker.UnexpectedResponseException;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Service
public class MysqlDockerTestHarness {
	@Autowired
	Docker docker;

	@Autowired
	ConcurrentTaskExecutor executor;

	Logger logger = LoggerFactory.getLogger(MysqlDockerTestHarness.class);

	public Container findContainer(String nameSought) {
		Containers containers = docker.containers();
		for (java.util.Iterator<Container> i = containers.all(); i.hasNext();) {
			Container c = i.next();
			String id = c.containerId();
			JsonValue names = c.get("Names");
			for (JsonValue name : names.asJsonArray()) {
				JsonString str = (JsonString) name;
				if (str.getString().equals("/" + nameSought)) {
					return c;
				}
			}
		}
		throw new NoContainerFoundException("No container with name:" + nameSought + " found");

	}

	public Container createMysqlContainer(String name, String mysqlImg, String rootPass,
			String databaseName) throws IOException {
		try {
			Container existing = findContainer(name);
			throw new ContainerExistsException("Container with name: " + name + " already exists");
		} catch (NoContainerFoundException e) {

		}
		JsonArrayBuilder env = Json.createArrayBuilder();
		env.add("MYSQL_ROOT_PASSWORD=" + rootPass);
		env.add("MYSQL_ROOT_HOST=%");
		env.add("MYSQL_DATABASE=" + databaseName);

		JsonObjectBuilder ports = Json.createObjectBuilder().add("3306/tcp",
				Json.createObjectBuilder().build());

		JsonObject createJson = Json.createObjectBuilder().add("Image", mysqlImg)//
				.add("Env", env.build())//
				.add("ExposedPorts", ports.build())
				.add("HostConfig", Json.createObjectBuilder().add("PublishAllPorts", true).build())//
				.build();
		// docker.containers().
		String[] arr = mysqlImg.split(":");
		docker.images().pull(arr[0], arr[1]);
		Container mysqlTest = docker.containers().create(name, createJson);
		return mysqlTest;
	}

	public void deleteMysqlContainer(String name) throws UnexpectedResponseException, IOException {
		Container container = this.findContainer(name);
		container.stop();
		container.remove();
	}

	public Boolean waitForLog(Container container, String regex, int timeout)
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		Pattern p = Pattern.compile(regex);
		try {
			return container.logs().follow(new ResponseHandler<Boolean>() {

				@Override
				public Boolean handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {

					Future<Boolean> f = executor.submit(() -> {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(response.getEntity().getContent()));
						String line = "";
						while (line != null) {
							try {
								line = br.readLine();
								logger.info("LOG> " + line);
								if (p.matcher(line).matches() || line.contains(regex)) {
									logger.info("Found matching line for regex: " + regex);
									return true;
								}
							} catch (IOException e) {
								logger.error("Failed to read log stream", e);
								return false;
							}
						}
						return false;
					});

					try {
						Boolean result = f.get(timeout, TimeUnit.MILLISECONDS);
						throw new DoneTailingLogException(result);

					} catch (TimeoutException e) {
						logger.info("Timed out waiting for regex: " + regex);
						throw new DoneTailingLogException(false);
					} catch (DoneTailingLogException e) {
						throw e;
					} catch (Exception e) {
						logger.error("Unexptected error waiting for regex: " + regex, e);
						throw new DoneTailingLogException(false);

					}
				}

			});
		} catch (DoneTailingLogException e) {
			return e.found;
		}

	}

	public DataSource createDataSource(Container container, String username, String password)
			throws IOException {
		JsonObject inspect = container.inspect();
		String pretty = JsonUtil.prettyPrint(inspect);
		logger.info(pretty);

		JsonValue network = inspect.get("NetworkSettings");//
		JsonString port = (JsonString) network.asJsonObject().get("Ports").asJsonObject()
				.get("3306/tcp").asJsonArray().get(0).asJsonObject().get("HostPort");
		JsonString ip = network.asJsonObject().getJsonString("IPAddress");

		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://%1s:%2s/integrationTest?serverTimezone=UTC";
		url = String.format(url, "127.0.0.1", port.getString());
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(username);
		dataSourceBuilder.password(password);
		DataSource dataSource = dataSourceBuilder.build();
		QueryExecutionListener listener = new QueryExecutionListener() {
			@Override
			public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {

			}

			@Override
			public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {

			}
		};
		ProxyDataSource proxyed = ProxyDataSourceBuilder.create(dataSource).listener(listener)
				.build();
		return proxyed;
	}
}
