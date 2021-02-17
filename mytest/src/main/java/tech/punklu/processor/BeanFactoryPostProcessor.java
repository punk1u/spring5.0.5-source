package tech.punklu.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;
import tech.punklu.service.TestService;

@Component
public class BeanFactoryPostProcessor implements org.springframework.beans.factory.config.BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanFactory.getBeanDefinition("userService");
		System.out.println(beanDefinition.getBeanClassName());
		/**
		 * 替换对应的Spring Bean工厂中的BeanDefinition，
		 * 将已经交给Spring管理的UserService中的Class对象替换为未
		 * 交给Spring管理的TestService，以实现替换类的效果。
		 * 之后，在Spring容器中使就可以使用getBean(TestService.class)获取TestService的实例了
		 */
		beanDefinition.setBeanClass(TestService.class);
	}
}
