package com.mgs.watermelon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Bruce
 */
@Controller
public class HelloController {

	@RequestMapping("/hello")
	public String hello(){
		System.out.println("hello");
		return "/index";
	}
	
	@RequestMapping("/index")
	public String index(){
		System.out.println("index");
		return "/index";
	}
}
