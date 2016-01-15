package com.hyf.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyf.exception.MyException;
import com.hyf.utils.ApiUtil;

@Controller
@RequestMapping("/test")
public class TestController
{

	@ResponseBody// 将该方法返回值转成json字符串输出,需要引入jackson开发包
	@RequestMapping(value = "/test.do", produces = "text/html;charset=UTF-8")
	public String testService(HttpServletRequest request)
	{
		try
		{
			String data = request.getParameter("data");// 获取页面传入的json数据
			if (data == null)
			{
				return "输入data参数为空.";
			}
			String result = ApiUtil.dubbo(request,ApiUtil.PROVIDER_SERVICE,"maplemart.provider.test.get",data);
			return result;
		}
		catch (Exception e)
		{
			throw new MyException(-1, "服务繁忙." + e.getMessage(),e);
		}
	}

	
}
