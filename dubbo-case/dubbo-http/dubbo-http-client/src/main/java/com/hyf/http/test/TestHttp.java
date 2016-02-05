package com.hyf.http.test;


import org.junit.Test;

import com.hyf.http.util.ApiUtil;

public class TestHttp
{
//	private String interfaceName = "maplemart.customer.test.get";
	private String data = "data={'id':2,'name':'df'}";

	// 测试http get请求
//	@Test
	public void httpGet()
	{
//		buf = doPost(paramsMap);（去HttpServerHandler类注解到这行代码和设置httpserver.properties里的httpserver.ssl为flase
//		，这样不用启动dubbo些程序 ）
		String result = ApiUtil.httpGet(data);
		System.out.println(result);
	}

	// 测试http post请求
//	@Test
	public void httpPost()
	{
//		buf = doPost(paramsMap);（去HttpServerHandler类注解到这行代码和设置httpserver.properties里的httpserver.ssl为flase
//		，这样不用启动dubbo些程序 ）
		String result = ApiUtil.httpPost(data);
		System.out.println(result);
	}

	// 测试http sslget请求
	@Test
	public void httpSSLGet()
	{
//		buf = doPost(paramsMap);（去HttpServerHandler类注解到这行代码和设置httpserver.properties里的httpserver.ssl为true
//		，这样不用启动dubbo些程序 ）
		String result = ApiUtil.httpSSLGet(data);
		System.out.println(result);
	}

	// 测试http sslpost请求
//	@Test
	public void httpSSLPost()
	{
//		buf = doPost(paramsMap);（去HttpServerHandler类注解到这行代码和设置httpserver.properties里的httpserver.ssl为true
//		，这样不用启动dubbo些程序 ）
		String result = ApiUtil.httpSSLPost(data);
		System.out.println(result);
	}

}
