package com.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

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
	
	//合并cookie中的购物车和数据库中的购物车
	@Override
	public E3Result mergeCart(long userId, List<TbItem> list) {
		//遍历列表		
		//把商品添加到购物车		
		//判断数据库中是否存在此商品		
		//如果是，那么数量相加
		//如果不是，那么增加商品
		for(TbItem tbItem : list){
			addCart(userId,tbItem.getId(),tbItem.getNum());
		}
		return E3Result.ok();
	}

	//根据用户ID，查询用户的购物车列表
	@Override
	public List<TbItem> getCartList(long userId) {
		//直接取redis中的hash的值即可
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemList = new ArrayList<TbItem>();
		for(String json : jsonList){
			itemList.add(JsonUtils.jsonToPojo(json, TbItem.class));
		}
		return itemList;
	}

	//更新购物车数量
	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		//从redis中取商品信息
		String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId+"");
		//更新商品数量
		TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		//写入redis
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		return E3Result.ok();
	}

	//删除购物车条目
	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		//删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE+":"+userId, itemId+"");
		return E3Result.ok();
	}

	//清空购物车
	@Override
	public E3Result clearCartItem(long userId) {
		jedisClient.del(REDIS_CART_PRE+":"+userId);
		return E3Result.ok();
	}
	

}
