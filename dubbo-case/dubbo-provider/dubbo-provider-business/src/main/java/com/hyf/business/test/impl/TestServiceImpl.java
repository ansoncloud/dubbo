package com.hyf.business.test.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyf.entity.RetStruct;

@Service
public class TestServiceImpl
{
	/**
	 * 测试，dubbo接口
	 * @interfaceName com.hyf.test.get
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String get(String data) throws Exception
	{
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("total", 1);
		result.put("rows", "{'get':'getdata'}");
		return new RetStruct("调用service服务成功.","data",result).toString();
	}
	
}
