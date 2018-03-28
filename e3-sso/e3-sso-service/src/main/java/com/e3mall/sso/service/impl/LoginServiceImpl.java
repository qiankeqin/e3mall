package com.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.e3mall.common.jedis.JedisClient;
import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.utils.JsonUtils;
import com.e3mall.mapper.TbUserMapper;
import com.e3mall.pojo.TbUser;
import com.e3mall.pojo.TbUserExample;
import com.e3mall.pojo.TbUserExample.Criteria;
import com.e3mall.sso.service.LoginService;

import redis.clients.jedis.Jedis;

/**
 * 登陆服务
 * @author qiankeqin
 *
 */
@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	/**
	 * 1.判断用户名和密码是否正确
	 * 2.如果不正确，返回登陆失败
	 * 3.如果正确，生成Token
	 * 4.把用户信息写入redis，key：token value：用户信息
	 * 5.设置session的过期时间
	 * 6.把token返回
	 */
	@Override
	public E3Result userLogin(String username, String password) {
		 //* 1.判断用户名和密码是否正确
		//根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list==null || list.size()==0){
			//2返回登陆失败
			return E3Result.build(400, "用户名或密码错误");
		}
		//取用户信息，判断密码是否正确
		TbUser user = list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			//2如果密码不正确
			return E3Result.build(400, "用户名或密码错误");
		}
		 //* 3.如果正确，生成Token
		String token = UUID.randomUUID().toString();
		 //* 4.把用户信息写入redis，key：token value：用户信息，为token加一个前缀
		//由于要将用户信息放到客户端，所以我们最好将密码清空了
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		 //* 5.设置session的过期时间
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		 //* 6.把token返回
		return E3Result.ok(token);
	}
	
	
}
