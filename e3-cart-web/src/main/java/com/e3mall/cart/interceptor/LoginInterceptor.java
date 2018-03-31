package com.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;

/**
 * 用户登陆处理拦截器
 * @author qiankeqin
 *
 */
public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired
	private TokenService tokenService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//前处理，执行handler之前执行此方法
		//返回true，放行，  false：拦截
		//1.从cookie中取token
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		//1.1如果没有token，未登录，直接放行
		if(StringUtils.isBlank(token)){
			return true;
		}
		//1.2取到token，需要调用sso系统的服务，根据token取用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		//2如果没有取到用户信息，登陆过期，直接放行
		if(e3Result.getStatus().intValue()!=200){
			return true;
		}
		//3.取到用户信息。登陆状态
		TbUser user = (TbUser) e3Result.getData();
		//4.把用户信息放到request中，只需要再Controller中判断request中是否包含user信息。放行
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		// handler执行之后，返回ModelAndView之前
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
			throws Exception {
		//完成处理，返回ModelAndView之后
		
		//可以再处理异常
		
	}


}
