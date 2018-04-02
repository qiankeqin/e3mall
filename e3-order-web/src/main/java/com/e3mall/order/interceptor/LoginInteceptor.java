package com.e3mall.order.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;

/**
 * 订单拦截器
 * @author qiankeqin
 *
 */
public class LoginInteceptor implements HandlerInterceptor {

	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	@Value("${COOKIE_USER_KEY}")
	private String COOKIE_USER_KEY;
	@Value("${SSO_URL}")
	private String SSO_URL;
	@Value("${COOKIE_CART_KEY}")
	private String COOKIE_CART_KEY;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//从cookie中取token，判断token是否存在
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		//如果不存在，不存在，未登陆状态，跳转到sso系统的登陆页面，登陆成功后，跳回到当前请求url
		if(StringUtils.isBlank(token)){
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}
		//如果存在，调用sso系统服务，根据token取用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		//如果取不到，登陆过期，需要重新登陆
		if(e3Result.getStatus()!=200){
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			//拦截
			return false;
		}
		//如果取到，那么时登陆状态，需要将用户信息写入request
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute(COOKIE_USER_KEY, user);
		//判断cookie中是否有购物车数据，合并到redis中
		//注意需要带上第三个参数，因为在加入之前时编码的，拿出来也必须编码，并且中文的原因，否则报错
		String jsonCartList = CookieUtils.getCookieValue(request, COOKIE_CART_KEY,true);
		if(StringUtils.isNotBlank(jsonCartList)){
			//合并购物车
			List<TbItem> itemList = JsonUtils.jsonToList(jsonCartList, TbItem.class);
			cartService.mergeCart(user.getId(), itemList);
		}
		//放行
		return true;
	}
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}


}
