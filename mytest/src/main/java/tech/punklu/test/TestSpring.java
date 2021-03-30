package tech.punklu.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tech.punklu.app.AppConfig;
import tech.punklu.bean.A;
import tech.punklu.circle.OrderService;

public class TestSpring {

	public static void main(String[] args) {
		/**
		 * 把类扫描出来
		 * 把bean实例化
		 */
		AnnotationConfigApplicationContext annotationConfigApplicationContext =
				new AnnotationConfigApplicationContext(AppConfig.class);
		//System.out.println(annotationConfigApplicationContext.getBean(UserService.class));
		System.out.println(annotationConfigApplicationContext.getBean(A.class));
		System.out.println(annotationConfigApplicationContext.getBean(A.class));
		annotationConfigApplicationContext.getBean(OrderService.class).blank();
	}
}
