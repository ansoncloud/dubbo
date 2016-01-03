package com.hyf.business.test.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.hyf.entity.RetStruct;
import com.hyf.global.GlobalContainer;
import com.hyf.openapi.interfaces.ApiInterface;
import com.hyf.utils.CheckJSONDataUtil;

@Service
public class TestServiceImpl
{
	public String getTestCom(String data) throws Exception
	{
		Map<String,Object> dataMap = CheckJSONDataUtil.checkJSONData(data);
		ApiInterface service = (ApiInterface) GlobalContainer.getApplicationContext().getBean("providerService");
		dataMap.put("method", "com.hyf.test.get");
		String resultdata = service.doPost(JSONObject.fromObject(dataMap).toString());
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("total", 1);
		result.put("rows", "消费者customerService服务的提供者TestServiceImpl类的get方法调用结果:"+resultdata);
		return new RetStruct("调用service服务成功.","data",result).toString();
	}
}
