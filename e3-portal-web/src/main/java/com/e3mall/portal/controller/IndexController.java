package com.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3mall.content.service.ContentService;
import com.e3mall.pojo.TbContent;

/**
 * 首页展示Controller
 * @author qiankeqin
 *
 */
@Controller
public class IndexController {
	
	@Value("${CONTENT_LUNBO_ID}")
	private Long CONTENT_LUNBO_ID;
	
	@Autowired
	private ContentService contentService;
	
	//当输入www.xxx.com的时候，因为没有匹配*.html,所以会去找index.html首页，所以映射到了index.html,并进入到控制器的handler中
	@RequestMapping(value="index.html")
	public String showIndex(Model model){
		//查询内容列表
		List<TbContent> contentList = contentService.getContentListById(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", contentList);
		return "index";
	}
}
