package com.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.mapper.TbOrderItemMapper;
import com.e3mall.mapper.TbOrderMapper;
import com.e3mall.mapper.TbOrderShippingMapper;
import com.e3mall.order.pojo.OrderInfo;
import com.e3mall.order.service.OrderService;
import com.e3mall.pojo.TbOrderItem;
import com.e3mall.pojo.TbOrderShipping;

/**
 * 订单处理服务
 * @author qiankeqin
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN;
	@Value("${ORDERITEM_ID_GEN_KEY}")
	private String ORDERITEM_ID_GEN;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	//创建订单
	@Override
	public E3Result createOrder(OrderInfo orderInfo) {
		//生成订单号，使用redis的incr生成
		if(!jedisClient.exists(ORDER_ID_GEN)){
			jedisClient.set(ORDER_ID_GEN, ORDER_ID_START);
		}
		//补全orderInfo的属性
		String orderId = jedisClient.incr("ORDER_ID_GEN").toString();
		orderInfo.setOrderId(orderId);
		//1、未付款，2、已付款、3、未发货、4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);//最好写成枚举类型放到Common中
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		
		//插入订单表
		orderMapper.insert(orderInfo);
		//插入订单明细
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for(TbOrderItem orderItem : orderItems){
			//补全pojo的属性
			String odId = jedisClient.incr(ORDERITEM_ID_GEN).toString();
			orderItem.setId(odId);
			orderItem.setOrderId(orderId);
			orderItemMapper.insert(orderItem);
		}
		//插入订单物流表数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		
		//返回成功，包含订单号
		return E3Result.ok(orderId);
	}
	

}
