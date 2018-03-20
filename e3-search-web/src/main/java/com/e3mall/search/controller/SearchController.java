package com.e3mall.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.SearchResult;
import com.e3mall.search.service.SearchService;

/**
 * 查询商品Controller
 * @author qiankeqin
 *
 */
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	
	@RequestMapping("/search")
	public String searItemList(String keyword,@RequestParam(defaultValue = "1") Integer page,Model model) throws Exception{
		//查询商品列表
		keyword = new String(keyword.getBytes("ISO-8859-1"),"utf-8");
		SearchResult result = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		//把这个结果传递给界面
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages",result.getTotalPages());
		model.addAttribute("page",page);
		model.addAttribute("recordCount",result.getRecordCount());
		model.addAttribute("itemList",result.getItemList());
		
		//int i = 1/0;
		
		//返回逻辑视图
		return "search";
	}
}
