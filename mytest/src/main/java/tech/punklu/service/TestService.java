package tech.punklu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.punklu.circle.OrderService;

@Service
public class TestService {

	@Autowired
	private OrderService orderService;


}
