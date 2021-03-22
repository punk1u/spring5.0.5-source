package tech.punklu.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tech.punklu.bean.A;
import tech.punklu.bean.B;

@ComponentScan("tech.punklu.bean")
@Configuration
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
