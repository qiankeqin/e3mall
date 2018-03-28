package com.e3mall.sso.service;

import com.e3mall.common.pojo.E3Result;

public interface LoginService {
	
	//参数：用户名和密码
	//业务逻辑
	/**
	 * 1.判断用户名和密码是否正确
	 * 2.如果不正确，返回登陆失败
	 * 3.如果正确，生成Token
	 * 4.把用户信息写入redis，key：token value：用户信息
	 * 5.设置session的过期时间
	 * 6.把token返回
	 */
	//返回值：E3Result，包含token
	E3Result userLogin(String username,String password);
	
	
}
