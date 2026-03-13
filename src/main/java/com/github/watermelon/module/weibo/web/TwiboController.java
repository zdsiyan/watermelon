package com.github.watermelon.module.weibo.web;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.watermelon.core.vo.ResultVO;
import com.github.watermelon.core.vo.SysDefinition;
import com.github.watermelon.module.common.entity.MUser;
import com.github.watermelon.module.common.service.MUserService;
import com.github.watermelon.module.weibo.entity.Twibo;
import com.github.watermelon.module.weibo.entity.Twicomment;
import com.github.watermelon.module.weibo.service.TwiboService;

@Slf4j
@Controller
@RequestMapping("/twibo")
public class TwiboController {
	
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
	@RequestMapping(value="/post/twibo")
	public ResultVO<Twibo> postTwibo(HttpSession session, String content){
		MUser user= (MUser)session.getAttribute(SysDefinition.USER_SESSION_KEY);
		if(user==null){
			return new ResultVO<Twibo>(SysDefinition.CODE_NODATA,null,null);
		}
		Twibo result = null;
		try{
			result = twiboService.postTwibo(content, user);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		if(result!=null){
			return new ResultVO<Twibo>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<Twibo>(SysDefinition.CODE_NODATA,null,null);
		}
	}
	
	/**
	 * 发表评论
	 * @param session
	 * @param tid
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/post/comment")
	public ResultVO<Twicomment> postComment(HttpSession session, String tid, String content){
		MUser user= (MUser)session.getAttribute(SysDefinition.USER_SESSION_KEY);
		if(user==null){
			return new ResultVO<Twicomment>(SysDefinition.CODE_NODATA,null,null);
		}
		Twicomment result = null;
		try{
			result = twiboService.postComment(tid, user, content);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		if(result!=null){
			return new ResultVO<Twicomment>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<Twicomment>(SysDefinition.CODE_NODATA,null,null);
		}
	}
	
	/**
	 * 获取评论
	 * @param tid
	 * @param offset
	 * @param length
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/comments")
	public ResultVO<List<Twicomment>> comments(String tid, Integer offset, Integer length){
		List<Twicomment> result = null;
		try{
			//TODO:未分页
			result = twiboService.getComments(tid, offset, length);
		}catch(Exception e){
			log.error(e.getMessage());
		}
		if(result!=null){
			return new ResultVO<List<Twicomment>>(SysDefinition.CODE_SUCCESS,null,result);
		}else{
			return new ResultVO<List<Twicomment>>(SysDefinition.CODE_NODATA,null,null);
		}
	}
	
	/**
	 * 列表获取twibo
	 * @param session
	 * @param offset
	 * @param length
	 * @return
	 */
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
			log.error(e.getMessage());
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
			Optional<MUser> muserOpt = mUserService.findById(oid);
			if(muserOpt.isPresent()){
				view.addObject("follows",muserOpt.get().getFollows());
			}
		}catch(Exception e){
			log.error(e.getMessage());
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
			Optional<MUser> muserOpt = mUserService.findById(oid);
			if(muserOpt.isPresent()){
				view.addObject("fans",muserOpt.get().getFans());
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value="/get")
	public ResultVO<Twibo> get(String oid){
		try{
			Optional<Twibo> resultOpt = twiboService.getTwiboById(oid);
			if(resultOpt.isPresent()){
				return new ResultVO<Twibo>(SysDefinition.CODE_SUCCESS,null,resultOpt.get());
			}else{
				return new ResultVO<Twibo>(SysDefinition.CODE_NODATA,null,null);
			}
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResultVO<Twibo>(SysDefinition.CODE_ERROR,null,null);
		}
	}

}
