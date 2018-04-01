package com.e3mall.cart.service;

import java.util.List;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbItem;

public interface CartService {
	E3Result addCart(long userId,long itemId,int num);
	E3Result mergeCart(long userId,List<TbItem> list);
	List<TbItem> getCartList(long userId);
	E3Result updateCartNum(long userId,long itemId,int num);
	E3Result deleteCartItem(long userId,long itemId);
}
