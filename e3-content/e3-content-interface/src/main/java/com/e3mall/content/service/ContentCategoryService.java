package com.e3mall.content.service;

import java.util.List;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUITreeNode;

/**
 * 内容分类列表服务
 * @author qiankeqin
 *
 */
public interface ContentCategoryService {
	//根据parentId获取内容目录
	List<EasyUITreeNode> getContentCatList(Long parentId);
	//新增内容分类
	E3Result addContentCategory(long parentId,String name);
}
