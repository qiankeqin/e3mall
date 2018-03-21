package com.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.IDUtils;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbItemDescMapper;
import com.e3mall.mapper.TbItemMapper;
import com.e3mall.pojo.EasyUIDataGridResult;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.pojo.TbItemExample;
import com.e3mall.pojo.TbItemExample.Criteria;
import com.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource
	private Destination topicDestination;
	
	@Autowired
	private JedisClient jedisClient;
	
	
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${REDIS_ITEM_CACHE_EXPIRE}")
	private Integer REDIS_ITEM_CACHE_EXPIRE;
	
	/**
	 * 根据ID获取Item对象
	 */
	@Override
	public TbItem getItemById(Long itemId) {
		//查询缓存
		try{
			String value = jedisClient.get(REDIS_ITEM_PRE+itemId+":BASE");
			if(StringUtils.isNotBlank(value)){
				TbItem tbItem = JsonUtils.jsonToPojo(value, TbItem.class);
				return tbItem;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		//缓存中没有，查询数据库			
		//根据主键查询
//		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria creteria = example.createCriteria();
		//设置条件
		creteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list!=null && list.size()>0){
			//把结果添加到数据库
			try{
				jedisClient.set(REDIS_ITEM_PRE+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
				//设置缓存的过期时间
				jedisClient.expire(REDIS_ITEM_PRE+itemId+":BASE", REDIS_ITEM_CACHE_EXPIRE);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	/**
	 * 分区商品分页数据
	 */
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
			
		return result;
	}

	/**
	 * 新增商品
	 */
	@Override
	public E3Result addItem(TbItem item, String desc) {
		//生成商品id
		final long itemId = IDUtils.genItemId();
		//补全商品信息
		item.setId(itemId);
		//向商品表插入数据
		//1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		
		//发送消息,但是这个时候有个问题，因为事务还没有提交
		//解决方法1）在消息消费的地方，取出消息后，等待一会，在进行查询
		//解决方法2）在事务提交后再进行发送消息，即再表现层发送。（更加保险）
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage text = session.createTextMessage(itemId+"");
				return text;
			}
		});
		
		//返回成功
		return E3Result.ok();
	}

	/**
	 * 根据商品id获取商品详情
	 */
	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询缓存
		try{
			String itemDescStr = jedisClient.get(REDIS_ITEM_PRE+itemId+":DESC");
			if(itemDescStr!=null && !"".equals(itemDescStr)){
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(itemDescStr, TbItemDesc.class);
				return itemDesc;				
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		//如果缓存中没有，查询数据库
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//把结果添加到数据库
		try{
			jedisClient.set(REDIS_ITEM_PRE+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
			//设置缓存的过期时间
			jedisClient.expire(REDIS_ITEM_PRE+itemId+":DESC", REDIS_ITEM_CACHE_EXPIRE);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return itemDesc;
	}
	
	

}
