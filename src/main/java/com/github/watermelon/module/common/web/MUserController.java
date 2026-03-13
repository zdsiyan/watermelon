package com.github.watermelon.module.common.web;

import com.github.watermelon.core.vo.ResultVO;
import com.github.watermelon.core.vo.SysDefinition;
import com.github.watermelon.module.common.entity.MUser;
import com.github.watermelon.module.common.service.MUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * 用户控制器
 * 使用Spring Boot 3.x和Jakarta EE API重构
 */
@Controller
@RequestMapping("/user")
public class MUserController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MUserController.class);

    @Autowired
    private MUserService mUserService;

    /**
     * 查询用户信息
     */
    @ResponseBody
    @GetMapping("/get")
    public ResultVO<MUser> get(String userName) {
        try {
            Optional<MUser> userOpt = mUserService.findByUserName(userName);
            if (userOpt.isPresent()) {
                return new ResultVO<>(SysDefinition.CODE_SUCCESS, null, userOpt.get());
            } else {
                return new ResultVO<>(SysDefinition.CODE_NODATA, null, null);
            }
        } catch (Exception e) {
            log.error("查询用户失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, null, null);
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ModelAndView register(HttpSession session, HttpServletResponse response, MUser param) {
        try {
            MUser result = mUserService.register(param);
            if (result != null) {
                session.setAttribute(SysDefinition.USER_SESSION_KEY, result);
                saveCookie(session, result, response);
                return new ModelAndView("redirect:/home");
            }
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage(), e);
        }
        return new ModelAndView("redirect:/signin");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ModelAndView login(HttpSession session, HttpServletResponse response,
                              @RequestParam String userName,
                              @RequestParam String password) {
        try {
            Optional<MUser> userOpt = mUserService.login(userName, password);
            if (userOpt.isPresent()) {
                MUser user = userOpt.get();
                session.setAttribute(SysDefinition.USER_SESSION_KEY, user);
                saveCookie(session, user, response);
                return new ModelAndView("redirect:/home");
            }
        } catch (Exception e) {
            log.error("用户登录失败: {}", e.getMessage(), e);
        }
        return new ModelAndView("redirect:/signin");
    }

    /**
     * 保存Cookie
     */
    private void saveCookie(HttpSession session, MUser user, HttpServletResponse response) {
        // 会话ID Cookie
        Cookie sessionCookie = new Cookie(SysDefinition.COOKIE_SESSIONID_KEY, session.getId());
        sessionCookie.setMaxAge(3600);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        // 登录ID Cookie
        Cookie loginIdCookie = new Cookie(SysDefinition.COOKIE_LOGINID_KEY, user.getUserName());
        loginIdCookie.setMaxAge(2592000); // 30天
        loginIdCookie.setPath("/");
        response.addCookie(loginIdCookie);
    }

    /**
     * 用户登出
     */
    @ResponseBody
    @PostMapping("/logout")
    public ResultVO<Object> logout(HttpSession session) {
        try {
            session.removeAttribute(SysDefinition.USER_SESSION_KEY);
            session.invalidate();
            return new ResultVO<>(SysDefinition.CODE_SUCCESS, "登出成功", null);
        } catch (Exception e) {
            log.error("用户登出失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, "登出失败", null);
        }
    }

    /**
     * 删除用户账号
     */
    @ResponseBody
    @PostMapping("/delete")
    public ResultVO<MUser> delete(HttpSession session, @RequestParam String password) {
        try {
            MUser currentUser = (MUser) session.getAttribute(SysDefinition.USER_SESSION_KEY);
            if (currentUser == null) {
                return new ResultVO<>(SysDefinition.CODE_NODATA, "用户未登录", null);
            }

            mUserService.delete(currentUser, password);
            session.removeAttribute(SysDefinition.USER_SESSION_KEY);
            session.invalidate();

            return new ResultVO<>(SysDefinition.CODE_SUCCESS, "账号删除成功", null);
        } catch (SecurityException e) {
            log.error("密码验证失败: {}", e.getMessage());
            return new ResultVO<>(SysDefinition.CODE_ERROR, "密码错误", null);
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, "删除失败", null);
        }
    }

    /**
     * 更新用户签名
     */
    @ResponseBody
    @PostMapping("/update/signature")
    public ResultVO<String> updateSignature(HttpSession session, @RequestParam String content) {
        try {
            MUser currentUser = (MUser) session.getAttribute(SysDefinition.USER_SESSION_KEY);
            if (currentUser == null) {
                return new ResultVO<>(SysDefinition.CODE_NODATA, "用户未登录", null);
            }

            mUserService.updateSignature(currentUser, content);
            session.setAttribute(SysDefinition.USER_SESSION_KEY, currentUser);

            return new ResultVO<>(SysDefinition.CODE_SUCCESS, "签名更新成功", content);
        } catch (Exception e) {
            log.error("更新签名失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, "更新失败", null);
        }
    }

    /**
     * 关注用户
     */
    @ResponseBody
    @PostMapping("/follow")
    public ResultVO<String> follow(HttpSession session, @RequestParam String userName) {
        try {
            MUser currentUser = (MUser) session.getAttribute(SysDefinition.USER_SESSION_KEY);
            if (currentUser == null) {
                return new ResultVO<>(SysDefinition.CODE_NODATA, "用户未登录", null);
            }

            boolean success = mUserService.follow(currentUser, userName);
            if (success) {
                return new ResultVO<>(SysDefinition.CODE_SUCCESS, "关注成功", null);
            } else {
                return new ResultVO<>(SysDefinition.CODE_ERROR, "关注失败", null);
            }
        } catch (Exception e) {
            log.error("关注用户失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, "关注失败", null);
        }
    }

    /**
     * 取消关注
     */
    @ResponseBody
    @PostMapping("/unfollow")
    public ResultVO<String> unfollow(HttpSession session, @RequestParam String userName) {
        try {
            MUser currentUser = (MUser) session.getAttribute(SysDefinition.USER_SESSION_KEY);
            if (currentUser == null) {
                return new ResultVO<>(SysDefinition.CODE_NODATA, "用户未登录", null);
            }

            boolean success = mUserService.unfollow(currentUser, userName);
            if (success) {
                return new ResultVO<>(SysDefinition.CODE_SUCCESS, "取消关注成功", null);
            } else {
                return new ResultVO<>(SysDefinition.CODE_ERROR, "取消关注失败", null);
            }
        } catch (Exception e) {
            log.error("取消关注失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, "取消关注失败", null);
        }
    }

    /**
     * 检查用户名是否可用
     */
    @ResponseBody
    @GetMapping("/check/username")
    public ResultVO<Boolean> checkUsername(@RequestParam String userName) {
        try {
            boolean available = mUserService.isUserNameAvailable(userName);
            return new ResultVO<>(SysDefinition.CODE_SUCCESS, null, available);
        } catch (Exception e) {
            log.error("检查用户名失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, "检查失败", false);
        }
    }

    /**
     * 检查昵称是否可用
     */
    @ResponseBody
    @GetMapping("/check/nickname")
    public ResultVO<Boolean> checkNickname(@RequestParam String nickName) {
        try {
            boolean available = mUserService.isNickNameAvailable(nickName);
            return new ResultVO<>(SysDefinition.CODE_SUCCESS, null, available);
        } catch (Exception e) {
            log.error("检查昵称失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, "检查失败", false);
        }
    }

    /**
     * 检查邮箱是否可用
     */
    @ResponseBody
    @GetMapping("/check/email")
    public ResultVO<Boolean> checkEmail(@RequestParam String email) {
        try {
            boolean available = mUserService.isEmailAvailable(email);
            return new ResultVO<>(SysDefinition.CODE_SUCCESS, null, available);
        } catch (Exception e) {
            log.error("检查邮箱失败: {}", e.getMessage(), e);
            return new ResultVO<>(SysDefinition.CODE_ERROR, "检查失败", false);
        }
    }
}