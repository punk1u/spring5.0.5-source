package tech.punklu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.punklu.bean.I;

import java.util.List;

@Component
public class TestListInject {

	/**
	 * 将接口的实现类注入List
	 */
	@Autowired
	private List<I> iList;

}
