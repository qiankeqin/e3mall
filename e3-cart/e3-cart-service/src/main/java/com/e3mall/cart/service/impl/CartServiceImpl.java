package com.e3mall.cart.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.cart.service.CartService;
import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.TbItem;
/**
 * 购物车Service
 * @author qiankeqin
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;
	//添加购物车操作
	@Override
	public E3Result addCart(long userId, long itemId,int num) {
		//向redis中添加购物车
		//数据类型时hash key：用户id，field：商品id，value：商品信息
		if(jedisClient.hexists(REDIS_CART_PRE+":"+userId, itemId+"")){
			//判断商品是否存在，如果存在数量相加
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId+"");
			//把json转换成TbItem
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			//写回redis
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
			return E3Result.ok();
		}
		//如果不存在，根据商品id取商品信息
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		String image = tbItem.getImage();
		if(StringUtils.isNotBlank(image)){
			tbItem.setImage(image.split(",")[0]);
		}
		//添加到购物车列表中
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		//返回成功
		return E3Result.ok();
	}

}
