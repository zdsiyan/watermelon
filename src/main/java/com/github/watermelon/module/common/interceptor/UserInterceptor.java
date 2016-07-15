package com.github.watermelon.module.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.watermelon.core.vo.SysDefinition;
import com.github.watermelon.module.common.entity.MUser;

/**
 * 登录状态检查
 * @author admin
 *
 */
public class UserInterceptor extends HandlerInterceptorAdapter{

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView mav) throws Exception {
		MUser result= (MUser)(request.getSession()).getAttribute(SysDefinition.USER_SESSION_KEY);
		if(result==null){
			mav.setViewName("redirect:/signin");
		}
	}

}
