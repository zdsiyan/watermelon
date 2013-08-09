package com.mgs.watermelon.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.morphia.query.Query;
import com.mgs.watermelon.entity.MUser;
import com.mgs.watermelon.service.MUserService;
import com.mgs.watermelon.vo.ResultVO;
import com.mgs.watermelon.vo.SysDefinition;

@Controller
@RequestMapping("/user")
public class MUserController {
	/**
	 * logger.
	 */
	private Logger logger=Logger.getLogger(getClass());
	
	@Autowired
	private MUserService mUserService;
	
	/**
	 * 查询信息
	 * @param username
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/get")
	public ResultVO<MUser> get(String username){
		MUser result=null;
		try{
			Query<MUser> query = mUserService.createQuery().filter("userName",username);
			result=mUserService.findOne(query);
		}catch(Exception e){
			logger.error(e.getMessage());
			return new ResultVO<MUser>(SysDefinition.CODE_ERROR,null,null);
		}
		if(result!=null){
			return new ResultVO<MUser>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<MUser>(SysDefinition.CODE_NODATA,null,null);
		}
	}
	
	/**
	 * 新增用户
	 * @param username
	 * @param password
	 * @param nickname
	 * @param email
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/register")
	public ResultVO<MUser> register(HttpSession session, String username, String password, String nickname, String email){
		MUser result=null;
		try{
			result=mUserService.register(username, password, nickname, email);
		}catch(Exception e){
			logger.error(e.getMessage());
			return new ResultVO<MUser>(SysDefinition.CODE_ERROR,null,null);
		}
		if(result!=null){
			session.setAttribute(SysDefinition.USER_SESSION_KEY, result);
			return new ResultVO<MUser>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<MUser>(SysDefinition.CODE_NODATA,null,null);
		}
	}
	
	/**
	 * 登录
	 * @param session
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login")
	public ResultVO<MUser> login(HttpSession session, MUser param){
		MUser result=null;
		try{
			result=mUserService.login(param);
		}catch(Exception e){
			logger.error(e.getMessage());
			return new ResultVO<MUser>(SysDefinition.CODE_ERROR,null,null);
		}
		if(result!=null){
			session.setAttribute(SysDefinition.USER_SESSION_KEY, result);
			return new ResultVO<MUser>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<MUser>(SysDefinition.CODE_NODATA,null,null);
		}
	}
	
	/**
	 * 登出
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/logout")
	public ResultVO<Object> logout(HttpSession session){
		session.removeAttribute(SysDefinition.USER_SESSION_KEY);
		return new ResultVO<Object>(SysDefinition.CODE_SUCCESS,null,null);
	}
	
	/**
	 * 删除当前账号, 删除必须是登录状态, 且需要提交密码验证身份
	 * @param session
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public ResultVO<MUser> delete(HttpSession session, String password){
		MUser result= (MUser)session.getAttribute(SysDefinition.USER_SESSION_KEY);
		if(result==null){
			return new ResultVO<MUser>(SysDefinition.CODE_NODATA,null,null);
		}
		try{
			mUserService.delete(result, password);
			session.removeAttribute(SysDefinition.USER_SESSION_KEY);
		}catch(Exception e){
			logger.error(e.getMessage());
			return new ResultVO<MUser>(SysDefinition.CODE_ERROR,null,null);
		}
		return new ResultVO<MUser>(SysDefinition.CODE_NODATA,null,null);
	}
	
	@ResponseBody
	@RequestMapping(value="/update/signature")
	public ResultVO<MUser> signature(HttpSession session, String content){
		MUser result= (MUser)session.getAttribute(SysDefinition.USER_SESSION_KEY);
		if(result==null){
			return new ResultVO<MUser>(SysDefinition.CODE_NODATA,null,null);
		}
		try{
			result=mUserService.signature(result, content);
			session.setAttribute(SysDefinition.USER_SESSION_KEY, result);
		}catch(Exception e){
			logger.error(e.getMessage());
			return new ResultVO<MUser>(SysDefinition.CODE_ERROR,null,null);
		}
		return new ResultVO<MUser>(SysDefinition.CODE_NODATA,null,null);
	}
	

}
