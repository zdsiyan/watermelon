package com.mgs.watermelon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mgs.watermelon.entity.MUser;
import com.mgs.watermelon.vo.SysDefinition;

/**
 * @author Bruce
 */
@Controller
public class IndexController {
	@RequestMapping("/")
	public String index(HttpSession session, HttpServletRequest request){
		System.out.println("index");
		request.getCookies();
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
	public ModelAndView home(HttpSession session){
		ModelAndView view = new ModelAndView("/home");
		MUser muser= (MUser)session.getAttribute(SysDefinition.USER_SESSION_KEY);
		if(muser!=null){
			view.addObject("user",muser);
		}
		return view;
	}
	
	@RequestMapping("/twibo")
	public String twibo(){
		System.out.println("twibo");
		return "/twibo";
	}
}
