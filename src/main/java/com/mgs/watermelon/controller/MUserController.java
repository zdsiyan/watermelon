package com.mgs.watermelon.controller;

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
	
	@ResponseBody
	@RequestMapping(value="/get")
	public ResultVO<MUser> get(String username){
		MUser result=null;
		try{
			Query query = mUserService.createQuery().filter("userName",username);
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
	
	@ResponseBody
	@RequestMapping(value="/save")
	public ResultVO<MUser> save(String username, String password, String nickname, String email){
		MUser result=new MUser();
		try{
			result.setUserName(username);
			result.setPassword(password);
			mUserService.save(result);
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

}
