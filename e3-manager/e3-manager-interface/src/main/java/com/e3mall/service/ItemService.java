package com.e3mall.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.EasyUIDataGridResult;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;

public interface ItemService {
	TbItem getItemById(Long itemId);
	EasyUIDataGridResult getItemList(int page,int rows);
	E3Result addItem(TbItem item,String desc);
	TbItemDesc getItemDescById(long itemId);
}
