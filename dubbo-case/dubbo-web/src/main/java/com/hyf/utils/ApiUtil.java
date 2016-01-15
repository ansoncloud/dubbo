package com.hyf.utils;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hyf.exception.MyException;
import com.hyf.openapi.interfaces.ApiInterface;

public class ApiUtil
{
	public final static String PROVIDER_SERVICE = "providerService";
	public final static String CUSTOMER_SERVICE = "customerService";

	/**
	 * 调用base服务
	 * @author HuangYongFeng
	 * @createtime 2015年7月21日
	 * @param request request对象
	 * @param session 工具session
	 * @param method 接口方法名称
	 * @param data 传入数据
	 * @return 调用完接口数据
	 */
	public static String dubbo(HttpServletRequest request,String serviceName,String method,String data)
	{
		try
		{
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			ApiInterface service = (ApiInterface) ctx.getBean(serviceName);
			JSONObject obj = JSONObject.fromObject(data);
			obj.put("method",method);
			String result = service.doPost(obj.toString());
			return result;
		}
		catch (Exception e)
		{
			throw new MyException(-1, "调用接口失败." + e.getMessage(),e);
		}
	}

	
	
}
