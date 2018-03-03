package com.e3mall.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.EasyUITreeNode;
import com.e3mall.content.service.ContentCategoryService;

/**
 * 内容菜单服务
 * @author qiankeqin
 *
 */
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 根据parentId获取菜单列表
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name="id",defaultValue="0") Long parentId){
		List<EasyUITreeNode> nodeList = new ArrayList<EasyUITreeNode>();
		nodeList = contentCategoryService.getContentCatList(parentId);
		return nodeList;
	}
	
	/**
	 * 添加内容菜单节点
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result createContentCategory(Long parentId,String name){
		//调用服务添加节点
		E3Result result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	
}
