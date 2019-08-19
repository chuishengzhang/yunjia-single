package com.zcs.yunjia.service.impl;

import com.zcs.yunjia.mapper.TestMapper;
import com.zcs.yunjia.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TestServiceImpl implements TestService {

	// 注入
	@Autowired
	private TestMapper mapper;

	// 查询当前时间
	@Override
	public String queryNow() {
		return mapper.queryNow();
	}

}
