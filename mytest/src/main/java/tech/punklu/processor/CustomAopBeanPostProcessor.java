package tech.punklu.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import tech.punklu.circle.UserService;

public class CustomAopBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof UserService){
			bean = CglibUtil.getProxy();
		}
		return bean;
	}
}
