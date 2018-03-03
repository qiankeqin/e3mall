package com.e3mall.content.service;

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
}
