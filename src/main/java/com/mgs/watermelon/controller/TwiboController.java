package com.mgs.watermelon.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mgs.watermelon.entity.MUser;
import com.mgs.watermelon.entity.Twibo;
import com.mgs.watermelon.service.MUserService;
import com.mgs.watermelon.service.TwiboService;
import com.mgs.watermelon.vo.ResultVO;
import com.mgs.watermelon.vo.SysDefinition;

@Controller
@RequestMapping("/twibo")
public class TwiboController {
	/**
	 * logger.
	 */
	private Logger logger=Logger.getLogger(getClass());
	
	@Autowired
	private TwiboService twiboService;
	
	@Autowired
	private MUserService mUserService;
	
	/**
	 * 发微博
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/post")
	public ResultVO<Twibo> post(HttpSession session, String content){
		MUser user= (MUser)session.getAttribute(SysDefinition.USER_SESSION_KEY);
		if(user==null){
			return new ResultVO<Twibo>(SysDefinition.CODE_NODATA,null,null);
		}
		Twibo result = null;
		try{
			result = new Twibo();
			result.setContent(content);
			result.setTimestamp(new Date().getTime());
			result.setUser(user);
			twiboService.save(result);
		}catch(Exception e){
			logger.error(e.getMessage());
			return new ResultVO<Twibo>(SysDefinition.CODE_ERROR,null,null);
		}
		if(result!=null){
			return new ResultVO<Twibo>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<Twibo>(SysDefinition.CODE_NODATA,null,null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/list")
	public ResultVO<List<Twibo>> list(HttpSession session, Integer offset, Integer length){
		MUser user= (MUser)session.getAttribute(SysDefinition.USER_SESSION_KEY);
		if(user==null){
			return new ResultVO<List<Twibo>>(SysDefinition.CODE_NODATA,null,null);
		}
		List<Twibo> result = null;
		try{
			result = twiboService.getList(user, offset, length);
		}catch(Exception e){
			logger.error(e.getMessage());
			return new ResultVO<List<Twibo>>(SysDefinition.CODE_ERROR,null,null);
		}
		if(result!=null){
			return new ResultVO<List<Twibo>>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<List<Twibo>>(SysDefinition.CODE_NODATA,null,null);
		}
	}
	
	/**
	 * 查询所有关注者
	 * @param session
	 * @param oid
	 * @return
	 */
	@RequestMapping(value="/{oid}/follow")
	public ModelAndView follow(HttpSession session, @PathVariable String oid){
		ModelAndView view = new ModelAndView("/follow");
		try{
			MUser muser= mUserService.get(new ObjectId(oid));
			if(muser!=null){
				view.addObject("follows",muser.getFollows());
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return view;
	}
	
	/**
	 * 查询所有粉丝
	 * @param session
	 * @param oid
	 * @return
	 */
	@RequestMapping(value="/{oid}/fans")
	public ModelAndView fans(HttpSession session, @PathVariable String oid){
		ModelAndView view = new ModelAndView("/fans");
		try{
			MUser muser= mUserService.get(new ObjectId(oid));
			if(muser!=null){
				view.addObject("fans",muser.getFans());
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value="/get")
	public ResultVO<Twibo> get(String oid){
		Twibo result = null;
		try{
			result = twiboService.get(new ObjectId(oid));
		}catch(Exception e){
			logger.error(e.getMessage());
			return new ResultVO<Twibo>(SysDefinition.CODE_ERROR,null,null);
		}
		if(result!=null){
			return new ResultVO<Twibo>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<Twibo>(SysDefinition.CODE_NODATA,null,null);
		}
	}

}
