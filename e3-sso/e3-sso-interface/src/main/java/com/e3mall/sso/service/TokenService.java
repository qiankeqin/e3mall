package com.e3mall.sso.service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.pojo.TbUser;

public interface TokenService {
	E3Result getUserByToken(String token);
}
