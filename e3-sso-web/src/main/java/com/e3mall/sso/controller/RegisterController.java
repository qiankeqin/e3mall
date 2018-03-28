package com.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.RegisterService;

/**
 * 注册功能Controller
 * @author qiankeqin
 *
 */
@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	//首页
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	//校验
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
		return registerService.checkData(param, type);
	}
	
	//注册
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result result = registerService.register(user);
		return result;
	}
}
