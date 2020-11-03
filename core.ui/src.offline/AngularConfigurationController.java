package com.lsq.core.ui.components.angular.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
public class AngularConfigurationController {

	AngularConfigurationBean config = new AngularConfigurationBean();

	@Value("${core.ui.api.base.url}")
	String baseUrl;

	@GetMapping("/config")
	AngularConfigurationBean getConfiguration() {
		config.apiBaseUrl = baseUrl;
		return config;
	}
}
