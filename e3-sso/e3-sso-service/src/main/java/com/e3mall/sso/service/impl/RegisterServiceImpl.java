package com.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import javax.imageio.spi.RegisterableService;
import javax.imageio.spi.ServiceRegistry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.mapper.TbUserMapper;
import com.e3mall.pojo.TbUser;
import com.e3mall.pojo.TbUserExample;
import com.e3mall.pojo.TbUserExample.Criteria;
import com.e3mall.sso.service.RegisterService;

/**
 * 注册服务
 * @author qiankeqin
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;
	
	//注册校验
	@Override
	public E3Result checkData(String param, int type) {
		//根据不同的type生成不同的查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1:用户名，2：手机号，3：邮箱
		switch(type){
			case 1:
				criteria.andUsernameEqualTo(param);
				break;
			case 2:
				criteria.andPhoneEqualTo(param);
				break;
			case 3:
				criteria.andEmailEqualTo(param);
				break;
			default:
				return E3Result.build(400, "数据类型错误！");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		//判断结果中是否有数据
		//如果有数据返回false，否则返回true
		if(list!=null && list.size()>0){
			return E3Result.ok(false);
		}else{
			return E3Result.ok(true);			
		}
	}

	//注册
	@Override
	public E3Result register(TbUser user) {
		//对数据的有效性进行校验
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getPassword())){
			return E3Result.build(400, "用户数据不完整，注册失败");
		}
		E3Result checkData = checkData(user.getUsername(),1);
		if(!(boolean) checkData.getData()){
			return E3Result.build(400, "此用户名已经被占用");
		}
		checkData = checkData(user.getPhone(),2);
		if(!(boolean) checkData.getData()){
			return E3Result.build(400, "手机号已经被占用");
		}
		//补全user，对密码进行md5加密，将用户数据插入数据库，返回添加成功
		user.setCreated(new Date());
		user.setUpdated(new Date());
		
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		
		userMapper.insert(user);
		
		return E3Result.ok();
	}
	

}
