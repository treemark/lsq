package com.lsq.core.rest.components.dispatch;

import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.stereotype.Service;

@Service
public class DispatcherConfig implements TomcatContextCustomizer {

	@Override
	public void customize(Context context) {

//		ServletRegistration.Dynamic dispatcher = container.addServlet("rentaltransui-dispatcher",
//				new DispatcherServlet(context));
//		dispatcher.setLoadOnStartup(1);
//		dispatcher.addMapping("/api/*");
//
//		Wrapper wrapper = Tomcat.addServlet(rootCtx, servletClass.getSimpleName() + "_apiproxy",
//				servletClass.getName());
//		wrapper.addInitParameter("targetUri", "http://localhost:8081/");
//		wrapper.addMapping("/api/*");
	}

}
