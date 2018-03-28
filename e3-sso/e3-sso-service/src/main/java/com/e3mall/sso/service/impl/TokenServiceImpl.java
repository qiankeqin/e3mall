package com.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.pojo.TbUser;
import com.e3mall.sso.service.TokenService;

/**
 * TokenService类，根据Token获取用户信息
 * @author qiankeqin
 *
 */
@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	//根据token获取User信息
	@Override
	public E3Result getUserByToken(String token) {
		//根据token到redis中获取信息
		String json = jedisClient.get("SESSION:"+token);
		//取不到信息，返回登陆过期
		if(StringUtils.isBlank(json)){
			return E3Result.build(201, "用户登陆已经过期");
		}
		//取到用户信息，更新token的过期时间，返回结果，E3Result其中包含TbUser信息
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(user);
	}

}
