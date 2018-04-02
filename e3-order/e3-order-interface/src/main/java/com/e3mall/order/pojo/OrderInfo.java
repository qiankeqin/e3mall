package com.e3mall.order.pojo;

import java.io.Serializable;
import java.util.List;

import com.e3mall.pojo.TbOrder;
import com.e3mall.pojo.TbOrderItem;
import com.e3mall.pojo.TbOrderShipping;

/**
 * 订单信息POJO
 * @author qiankeqin
 *
 */
public class OrderInfo extends TbOrder implements Serializable {
	public List<TbOrderItem> orderItems;
	private TbOrderShipping orderShipping;
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
	
	
}
