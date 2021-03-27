package tech.punklu.processor;

import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import tech.punklu.circle.UserService;

import java.lang.reflect.Method;

public class CglibUtil {

	public static Object getProxy(){
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(UserService.class);
		enhancer.setUseFactory(false);
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
				System.out.println("aop---");
				Object result = methodProxy.invokeSuper(o, objects);
				return result;
			}
		});
		Object o = enhancer.create();
		return o;
	}
}
