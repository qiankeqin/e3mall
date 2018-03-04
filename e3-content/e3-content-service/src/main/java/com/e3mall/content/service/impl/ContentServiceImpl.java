package com.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.content.service.ContentService;
import com.e3mall.mapper.TbContentMapper;
import com.e3mall.pojo.TbContent;
import com.e3mall.pojo.TbContentExample;
import com.e3mall.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Override
	public E3Result addContent(TbContent content) {
		content.setUpdated(new Date());
		content.setCreated(new Date());
		contentMapper.insert(content);
		return E3Result.ok();
	}

	@Override
	public List<TbContent> getContentListById(Long cid) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		//查询包含大文本的数据字段
		List<TbContent> contentList = contentMapper.selectByExampleWithBLOBs(example);
		return contentList;
	}

}
