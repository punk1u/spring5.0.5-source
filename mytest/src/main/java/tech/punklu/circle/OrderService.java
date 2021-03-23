package tech.punklu.circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

	@Autowired
	private UserService userService;

	public void blank(){

	}
}
