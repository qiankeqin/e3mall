package com.e3mall.cart.service;

import com.e3mall.common.pojo.E3Result;

public interface CartService {
	E3Result addCart(long userId,long itemId,int num);
}
