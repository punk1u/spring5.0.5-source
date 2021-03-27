package tech.punklu.app;

import org.springframework.context.annotation.*;
import tech.punklu.bean.A;
import tech.punklu.bean.B;
import tech.punklu.processor.CustomAopBeanPostProcessor;

@ComponentScan("tech.punklu")
@Configuration
@EnableAspectJAutoProxy
@Import(CustomAopBeanPostProcessor.class)
public class AppConfig {

	@Bean
	public A a(){
		System.out.println("a init");
		return new A();
	}

	@Bean
	public B b(){
		System.out.println("b init");
		a();
		return new B();
	}
}
