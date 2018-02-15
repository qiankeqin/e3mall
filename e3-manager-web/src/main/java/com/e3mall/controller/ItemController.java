package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.pojo.EasyUIDataGridResult;
import com.e3mall.pojo.TbItem;
import com.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem item = itemService.getItemById(itemId);
		return item;
	}
	
	//分页查询
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		//调用服务查询商品列表
		EasyUIDataGridResult itemList = itemService.getItemList(page, rows);
		return itemList;
	}
}
