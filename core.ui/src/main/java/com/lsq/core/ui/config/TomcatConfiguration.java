package com.lsq.core.ui.config;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.lsq.core.ui.components.CoreUIMarker;
import com.lsq.core.ui.components.proxy.ProxyServlet;

@Configuration
@PropertySource("classpath:core.ui.properties")
@ComponentScan(basePackageClasses = { CoreUIMarker.class }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) })
public class TomcatConfiguration implements WebMvcConfigurer {

	@Value("${core.ui.http.port}")
	Integer portNumber;

	@Autowired(required = false)
	List<TomcatContextCustomizer> customizers;

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.setPort(this.portNumber);
		factory.setContextPath("");
		// factory.setBaseDirectory(baseDirectory);

		factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));
		TomcatContextCustomizer ctxCustomizer = new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				if (customizers != null)
					for (TomcatContextCustomizer customizer : customizers) {
						customizer.customize(context);
					}
			}
		};
		factory.addContextCustomizers(ctxCustomizer);
		return factory;
	}

	static int count = 0;

	private static void configureProxy(Context rootCtx, String mapping, String destination) {
		Class servletClass = ProxyServlet.class;

		Wrapper wrapper = Tomcat.addServlet(rootCtx, servletClass.getSimpleName() + "_" + count,
				servletClass.getName());
		count++;
		wrapper.addInitParameter("targetUri", destination);
		wrapper.addMapping(mapping);
		// wrapper.addInitParameter("log", "true");
	}

	// @Bean
	public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
		return new WebMvcRegistrations() {
			@Override
			public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
				return new RequestMappingHandlerMapping() {
					private final static String API_BASE_PATH = "api";

					@Override
					protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
						Class<?> beanType = method.getDeclaringClass();
						if (AnnotationUtils.findAnnotation(beanType, RestController.class) != null) {
							PatternsRequestCondition apiPattern = new PatternsRequestCondition(API_BASE_PATH)
									.combine(mapping.getPatternsCondition());

							mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
									mapping.getMethodsCondition(), mapping.getParamsCondition(),
									mapping.getHeadersCondition(), mapping.getConsumesCondition(),
									mapping.getProducesCondition(), mapping.getCustomCondition());
						}

						super.registerHandlerMethod(handler, method, mapping);
					}
				};
			}
		};
	}

}
