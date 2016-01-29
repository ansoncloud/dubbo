package com.hyf.utils;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hyf.openapi.interfaces.ApiInterface;

public class ApiUtil
{
	public final static String PROVIDER_SERVICE = "providerService";
	public final static String CUSTOMER_SERVICE = "customerService";

	/**
	 * 调用base服务
	 * @author HuangYongFeng
	 * @createtime 2015年7月21日
	 * @param serviceName 服务名称
	 * @param method 接口方法名称
	 * @param data 传入数据
	 * @return 调用完接口数据
	 */
	@SuppressWarnings("resource")
	public static String dubbo(String serviceName,String interfaceName,String data)
	{
			ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
			ApiInterface service = (ApiInterface) ctx.getBean(serviceName);
			JSONObject obj = JSONObject.fromObject(data);
			obj.put("interfaceName",interfaceName);
			obj.put("data", data);
			String result = service.doPost(obj.toString());
			return result;
	}

	
	
}
