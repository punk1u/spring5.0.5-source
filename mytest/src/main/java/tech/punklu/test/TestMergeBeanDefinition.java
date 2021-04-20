package tech.punklu.test;

import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tech.punklu.app.AppConfig;
import tech.punklu.bean.A;
import tech.punklu.bean.ChildBean;
import tech.punklu.bean.RootBean;
import tech.punklu.circle.OrderService;

public class TestMergeBeanDefinition {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext =
				new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(AppConfig.class);

		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
		rootBeanDefinition.setBeanClass(RootBean.class);
		rootBeanDefinition.getPropertyValues().add("type","movie");
		rootBeanDefinition.getPropertyValues().add("name","image");
		annotationConfigApplicationContext.registerBeanDefinition("root",rootBeanDefinition);

		GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
		genericBeanDefinition.setParentName("root");
		genericBeanDefinition.setBeanClass(ChildBean.class);
		genericBeanDefinition.getPropertyValues().add("name","枪火");
		annotationConfigApplicationContext.registerBeanDefinition("child",genericBeanDefinition);

		annotationConfigApplicationContext.refresh();

		System.out.println(annotationConfigApplicationContext.getBean(ChildBean.class));
	}
}
