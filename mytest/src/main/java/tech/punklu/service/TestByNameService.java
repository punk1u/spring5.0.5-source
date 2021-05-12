package tech.punklu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.punklu.bean.I;

@Component
public class TestByNameService {

	/**
	 * I是接口，其实现类有X,Y如果这里不将字段名命名为x，y而是
	 * 命名为i的话会报错，因为Spring会不知道使用X、Y中的哪一个，
	 * 只有明确指明使用x，y中的某一个时才不会报错并完成自动注入
	 *
	 * 具体处理逻辑见：
	 * DefaultListableBeanFactory.doResolveDependency()方法
	 */
	@Autowired
	private I x;
}
