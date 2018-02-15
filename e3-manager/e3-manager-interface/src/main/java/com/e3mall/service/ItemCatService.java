package com.e3mall.service;

import java.util.List;

import com.e3mall.pojo.EasyUITreeNode;

public interface ItemCatService {
	List<EasyUITreeNode> getItemCatList(Long parentId);
}
