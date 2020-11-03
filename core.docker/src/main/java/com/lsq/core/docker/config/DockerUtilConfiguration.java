package com.lsq.core.docker.config;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

import com.amihaiemil.docker.Docker;
import com.amihaiemil.docker.LocalDocker;
import com.amihaiemil.docker.RemoteDocker;
import com.lsq.core.docker.components.mysql.CoreDockerMarker;

@Configuration()
@ConfigurationProperties(prefix = "security.db")
@PropertySource("classpath:docker.util.properties")
@ComponentScan(basePackageClasses = { CoreDockerMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
public class DockerUtilConfiguration {

	@Value(value = "${docker.mid.host}")
	String dockerHost;

	@Bean
	@Qualifier("dockerClient")
	public Docker dockerClient(HttpClient httpClient) throws URISyntaxException {
		File socket = new File("/var/run/docker.sock");
		if (socket.exists()) {
			Docker docker = new LocalDocker(socket);
			return docker;
		} else {
			Docker docker = new RemoteDocker(httpClient, new URI("http://localhost:2375"));
			return docker;
		}
	}
}
