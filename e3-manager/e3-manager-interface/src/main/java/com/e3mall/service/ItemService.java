package com.e3mall.service;

import com.e3mall.pojo.EasyUIDataGridResult;
import com.e3mall.pojo.TbItem;

public interface ItemService {
	TbItem getItemById(Long itemId);
	EasyUIDataGridResult getItemList(int page,int rows);
}
