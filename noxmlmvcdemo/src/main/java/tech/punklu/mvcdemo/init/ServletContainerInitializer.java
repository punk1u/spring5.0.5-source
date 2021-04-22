package tech.punklu.mvcdemo.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class ServletContainerInitializer implements javax.servlet.ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
		System.out.println("容器启动中....");
	}
}
