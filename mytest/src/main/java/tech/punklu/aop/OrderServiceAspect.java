package tech.punklu.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class OrderServiceAspect {

	@Pointcut("within(tech.punklu.circle.OrderService)")
	private void pointCut(){}

	@Before("pointCut()")
	public void doCheck(){
		System.out.println("aop before----");
	}
}
