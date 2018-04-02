package com.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.order.service.OrderService;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;

/**
 * 订单管理Controller
 * @author qiankeqin
 *
 */
@Controller
public class OrderController {
	@Autowired
	private CartService cartService;
	@Value("${COOKIE_USER_KEY}")
	private String COOKIE_USER_KEY;
	@Autowired
	private OrderService orderService;
	
	//展示订单确认页面
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		//获取登陆用户
		TbUser user = (TbUser) request.getAttribute(COOKIE_USER_KEY);
		//根据用户id取收获地址列表，这里使用静态数据
		//获取支付方式列表，这里使用静态数据
		//根据登陆用户，获取购物车列表
		List<TbItem> cartList = cartService.getCartList(user.getId());
		//把购物车列表传递给jsp
		request.setAttribute("cartList", cartList);
		return "order-cart";
	}
	
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		//取用户信息，将用户信息添加到orderInfo中
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//调用服务生成订单
		E3Result e3Result = orderService.createOrder(orderInfo);
		//如果生成订单成功，需要删除购物车
		if(e3Result.getStatus()==200){
			cartService.clearCartItem(user.getId());
		}
		//将订单号传递给页面
		request.setAttribute("orderId", e3Result.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		//返回逻辑视图
		return "success";
	}
}
