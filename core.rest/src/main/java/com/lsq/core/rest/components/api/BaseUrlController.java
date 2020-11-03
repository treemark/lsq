package com.lsq.core.rest.components.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseUrlController {
	@Value("${core.rest.api.base.url}")
	String baseUrl;

	@RequestMapping("/baseUrl")
	public String baseUrl() {
		return baseUrl;
	}
}
