package com.mgs.watermelon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Bruce
 */
@Controller
public class IndexController {
	@RequestMapping("/")
	public String index(){
		System.out.println("index");
		return "/index";
	}

	@RequestMapping("/test")
	public String test(){
		System.out.println("test");
		return "/test";
	}
	
	@RequestMapping("/signin")
	public String signin(){
		return "/signin";
	}
	
	@RequestMapping("/register")
	public String register(){
		return "/register";
	}
	
	@RequestMapping("/home")
	public String home(){
		return "/home";
	}
	
	@RequestMapping("/twibo")
	public String twibo(){
		System.out.println("twibo");
		return "/twibo";
	}
}
