package com.e3mall.content.service;

import java.util.List;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbContent;

/**
 * 内容管理
 * @author qiankeqin
 *
 */
public interface ContentService {
	//新增内容
	public E3Result addContent(TbContent content);
	//根据内容id获取内容
	List<TbContent> getContentListById(Long cid);
}
