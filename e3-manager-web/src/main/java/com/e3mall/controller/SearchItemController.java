package com.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.search.service.SearchItemService;

@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;
	
	/**
	 * 将商品数据导入到Solr索引库中
	 * @return
	 */
	@RequestMapping(value="/index/item/import")
	@ResponseBody
	public E3Result importItemList(){
		E3Result e3Result = searchItemService.importAllItems();
		return e3Result;
	}
}
