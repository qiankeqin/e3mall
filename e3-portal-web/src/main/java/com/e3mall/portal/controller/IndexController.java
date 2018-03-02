package com.e3mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页展示Controller
 * @author qiankeqin
 *
 */
@Controller
public class IndexController {
	
	//当输入www.xxx.com的时候，因为没有匹配*.html,所以会去找index.html首页，所以映射到了index.html,并进入到控制器的handler中
	@RequestMapping(value="index.html")
	public String showIndex(){
		return "index";
	}
}
