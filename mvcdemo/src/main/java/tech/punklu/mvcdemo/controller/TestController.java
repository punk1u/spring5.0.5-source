package tech.punklu.mvcdemo.controller;

import tech.punklu.mvcdemo.annotation.Controller;
import tech.punklu.mvcdemo.annotation.RequestMapping;

@Controller
@RequestMapping("/testController")
public class TestController {

	@RequestMapping("/testMapping.do")
	public void test(){

	}
}
