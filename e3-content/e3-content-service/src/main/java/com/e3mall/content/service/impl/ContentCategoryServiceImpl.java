package com.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUITreeNode;
import com.e3mall.content.service.ContentCategoryService;
import com.e3mall.mapper.TbContentCategoryMapper;
import com.e3mall.pojo.TbContentCategory;
import com.e3mall.pojo.TbContentCategoryExample;
import com.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service("contentCategoryServiceImpl")
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(Long parentId) {
		//根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
		//转换成EasyUITreeNode列表
		List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
		for(TbContentCategory catItem : contentCategoryList){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(catItem.getId());
			node.setText(catItem.getName());
			node.setState(catItem.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		return nodeList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		//创建一个tb_content_category表对应的pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		//设置pojo属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//插入到数据库
		//1:正常,2删除
		contentCategory.setStatus(1);
		//默认排序就是1
		contentCategory.setSortOrder(1);
		contentCategory.setUpdated(new Date());
		contentCategory.setCreated(new Date());
		//新添加的节点一定是叶子节点
		contentCategory.setIsParent(false);
		contentCategoryMapper.insert(contentCategory);
		
		//判断父节点的isparent属性，如果不是true，改为true（反正都是要改成true）
		//根据parentId获取父节点
		TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentCategory.getIsParent()){
			parentCategory.setIsParent(true);
			//更新数据库
			contentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		//返回结果E3Result,包含pojo		
		return E3Result.ok(contentCategory);
	}
	
}
