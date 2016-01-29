package com.hyf.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyf.utils.ApiUtil;

@Controller
@RequestMapping("/test")
public class TestController
{

	@ResponseBody// 将该方法返回值转成json字符串输出,需要引入jackson开发包
	@RequestMapping(value = "/testProvider.do",produces = "text/html;charset=UTF-8")
	public String testProviderService(HttpServletRequest request)
	{
		// String data = request.getParameter("data");// 获取页面传入的json数据
		// if (data == null)
		// {
		// return "输入data参数为空.";
		// }
		JSONObject obj = new JSONObject();
		obj.put("id", 1);
		obj.put("name", "姓名");
		String result = ApiUtil.dubbo(ApiUtil.PROVIDER_SERVICE, "maplemart.provider.test.get", obj.toString());
		return result;
	}
	
	@ResponseBody// 将该方法返回值转成json字符串输出,需要引入jackson开发包
	@RequestMapping(value = "/testCustomer.do", produces = "text/html;charset=UTF-8")
	public String testCustomerService(HttpServletRequest request)
	{
		// String data = request.getParameter("data");// 获取页面传入的json数据
		// if (data == null)
		// {
		// return "输入data参数为空.";
		// }
		JSONObject obj = new JSONObject();
		obj.put("id", 1);
		obj.put("name", "姓名");
		String result = ApiUtil.dubbo(ApiUtil.CUSTOMER_SERVICE, "maplemart.customer.test.get", obj.toString());
		return result;
	}

}
