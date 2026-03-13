package com.github.watermelon.module.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.github.watermelon.core.vo.SysDefinition;
import com.github.watermelon.module.common.entity.MUser;

/**
 * 登录状态检查，临时处理，以后调整
 * @author admin
 */
public class UserInterceptor implements HandlerInterceptor {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView mav) throws Exception {
		MUser result= (MUser)(request.getSession()).getAttribute(SysDefinition.USER_SESSION_KEY);
		if(result==null){
			mav.setViewName("redirect:/signin");
		}
	}

}
