package tech.punklu.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tech.punklu.app.AppConfig;
import tech.punklu.circle.OrderService;
import tech.punklu.circle.UserService;

public class TestCircle {

	public static void main(String[] args) {
		/**
		 * 把类扫描出来
		 * 把bean实例化
		 */
		AnnotationConfigApplicationContext annotationConfigApplicationContext =
				new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(annotationConfigApplicationContext.getBean(OrderService.class));
		System.out.println(annotationConfigApplicationContext.getBean(UserService.class));

	}
}
