package com.lsq.security.ui.components;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.stereotype.Service;

import com.lsq.core.ui.components.proxy.ProxyServlet;

@Service
public class ProxyConfiguration implements TomcatContextCustomizer {

	@Override
	public void customize(Context rootCtx) {
		Class servletClass = ProxyServlet.class;

		Wrapper wrapper = Tomcat.addServlet(rootCtx, servletClass.getSimpleName() + "_apiproxy",
				servletClass.getName());
		wrapper.addInitParameter("targetUri", "http://localhost:8081/");
		wrapper.addMapping("/api/*");
	}

}
