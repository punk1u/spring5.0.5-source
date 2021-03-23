package tech.punklu.circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private OrderService orderService;

	public void blank(){

	}
}
