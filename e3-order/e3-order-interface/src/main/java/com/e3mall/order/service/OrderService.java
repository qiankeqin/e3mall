package com.e3mall.order.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.order.pojo.OrderInfo;

public interface OrderService {
	E3Result createOrder(OrderInfo orderInfo);
}
