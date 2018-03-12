package com.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.content.service.ContentService;
import com.e3mall.mapper.TbContentMapper;
import com.e3mall.pojo.TbContent;
import com.e3mall.pojo.TbContentExample;
import com.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value(value="${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Override
	public E3Result addContent(TbContent content) {
		content.setUpdated(new Date());
		content.setCreated(new Date());
		contentMapper.insert(content);
		try{
			//删除缓存中对应的数据
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId()+"");
		}catch(Exception ex){
			ex.printStackTrace();			
		}
		return E3Result.ok();
	}

	/**
	 * 首页轮播图数据查询
	 * 注意，无论是查询缓存还是添加数据到缓存中，都不能影响正常的业务
	 */
	@Override
	public List<TbContent> getContentListById(Long cid) {
		//查询缓存
		try{
			//如果缓存中有直接响应
			String json = jedisClient.hget(CONTENT_LIST,cid+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		//如果没有，查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		//查询包含大文本的数据字段
		List<TbContent> contentList = contentMapper.selectByExampleWithBLOBs(example);
		//将查询到的数据存储到缓存中
		try{
			//redis中存储字符串
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(contentList));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return contentList;
	}

}
