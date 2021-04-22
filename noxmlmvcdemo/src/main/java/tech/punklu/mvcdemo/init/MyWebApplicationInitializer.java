package tech.punklu.mvcdemo.init;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import tech.punklu.mvcdemo.config.AppConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * 通过自定义的实现WebApplicationInitializer接口的实现类，可以达到不使用web.xml
 * 配置spring mvc的目的
 */
public class MyWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//初始化spring 容器  以注解的方式
		AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
		ac.register(AppConfig.class);
//        ac.setServletContext(servletCxt);
//        ac.refresh();
		DispatcherServlet servlet = new DispatcherServlet(ac);
		ServletRegistration.Dynamic registration = servletContext.addServlet("app", servlet);
		registration.setLoadOnStartup(1);
//        registration.setInitParameter("contextConfigLocation","spring mvc.xml 的地址");
		registration.addMapping("*.do");
	}
}
