package com.lsq.core.rest.components.hello;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Service
@RestController
@Api(tags = { "Hello" })
public class HelloController {

	@RequestMapping(path = "/hello", method = RequestMethod.GET)
	public String index() {
		return "Greetings from Spring Boot!";
	}

}