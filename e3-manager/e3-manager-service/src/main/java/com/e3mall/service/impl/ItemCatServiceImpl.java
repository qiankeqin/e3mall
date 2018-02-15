package com.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.mapper.TbItemCatMapper;
import com.e3mall.pojo.EasyUITreeNode;
import com.e3mall.pojo.TbItemCat;
import com.e3mall.pojo.TbItemCatExample;
import com.e3mall.pojo.TbItemCatExample.Criteria;
import com.e3mall.service.ItemCatService;

/**
 * 商品分类管理
 * @author qiankeqin
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	/**
	 * 根据parentId查询菜单列表
	 */
	@Override
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		//查询tbItemCat数据
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		Criteria criteria = tbItemCatExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//查询
		List<TbItemCat> list = tbItemCatMapper.selectByExample(tbItemCatExample);
		//将列表转成EasyUITreeNode数据
		//插件返回结果
		List<EasyUITreeNode> listNode = new ArrayList<EasyUITreeNode>();
		for(TbItemCat item:list){
			EasyUITreeNode treeNode = new EasyUITreeNode();
			treeNode.setId(item.getId());
			treeNode.setText(item.getName());
			treeNode.setState(item.getIsParent()?"closed":"open");
			listNode.add(treeNode);
		}
		return listNode;
	}

}
