package tech.punklu.mvcdemo.controller;

import tech.punklu.mvcdemo.annotation.Controller;
import tech.punklu.mvcdemo.annotation.RequestMapping;
import tech.punklu.mvcdemo.annotation.ResponseBody;
import tech.punklu.mvcdemo.entity.UserEntity;

@Controller
@RequestMapping("/testController")
public class TestController {

	@RequestMapping("/testMapping.do")
	@ResponseBody
	public String test(String name, String age, UserEntity userEntity){
		System.out.println(name);
		System.out.println(age);
		System.out.println(userEntity);
		System.out.println("执行了");
		return "testMapping.do";
	}

	@RequestMapping("/model.do")
	public Object model(){
 		return "index";
	}
}
