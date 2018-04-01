package com.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.CookieUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbUser;
import com.e3mall.service.ItemService;

/**
 * 购物车Controller
 * @author qiankeqin
 *
 */
@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Value("${COOKIE_CART_KEY}")
	private String COOKIE_CART_KEY;
	@Value("${COOKIE_USER_KEY}")
	private String COOKIE_USER_KEY;
	
	//显示购物车
	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request,HttpServletResponse response){
		//判断用户是否为登陆状态
		TbUser user = (TbUser)request.getAttribute(COOKIE_USER_KEY);
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//如果时登陆状态
		if(user!=null){
			//如果不为空，把cookie中的购物车商品和服务端的购物车商品合并
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, COOKIE_CART_KEY);
			//从服务端取购物车列表
			cartList = cartService.getCartList(user.getId());
		}		
		//未登陆状态
		//把参数传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "cart";
	}
	
	//加入商品到购物车
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//首先判断用户是否登陆
		TbUser user = (TbUser) request.getAttribute(COOKIE_USER_KEY);
		//如果时登陆状态，把购物车写入到redis
		if(user!=null){
			//保存到服务端
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		//如果未登陆，把购物车写入到cookie
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断商品在商品列表中是否存在
		boolean flag = false;
		for(TbItem item : cartList){
			if(item.getId()==itemId.longValue()){
				//找到商品，数量相加
				item.setNum(item.getNum()+num);
				flag = true;
				break;
			}
		}
		if(!flag){
			//如果不存在，根据商品id查询商品信息，得到一个TbItem对象
			TbItem tbItem = itemService.getItemById(itemId);
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)){
				String[] images = image.split(",");
				tbItem.setImage(images[0]);
			}
			//把商品添加到商品列表
			cartList.add(tbItem);
		}		
		//写入cookie
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		//返回添加成功页面
		return "cartSuccess";
	}
	
	//更新购物车商品数量
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断用户是否为登陆状态
		TbUser user = (TbUser) request.getAttribute(COOKIE_USER_KEY);
		if(user!=null){
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		//从cookie中取出购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历商品列表，找到商品
		for(TbItem tbItem : cartList){
			if(tbItem.getId() == itemId.longValue()){
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//把购物车列表写回cookie
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		//返回成功
		return E3Result.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,
			HttpServletResponse response){
		//判断用户是否为登陆状态
		TbUser user = (TbUser) request.getAttribute(COOKIE_USER_KEY);
		if(user!=null){
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		//从cookie中取商品列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//找到商品，删除
		for(TbItem tbItem : cartList){
			if(tbItem.getId()==itemId.longValue()){
				cartList.remove(tbItem);
				//跳出循环
				break;
			}
		}
		//把商品列表写回cookie
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		//跳转页面
		return "redirect:/cart/cart.html";
	}
	
	//从cookie中获取到商品列表
	private List<TbItem> getCartListFromCookie(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, COOKIE_CART_KEY, true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<TbItem>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	
}
