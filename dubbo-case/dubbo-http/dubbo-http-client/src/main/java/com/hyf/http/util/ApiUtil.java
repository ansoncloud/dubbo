package com.hyf.http.util;


public class ApiUtil
{
	
	/**
	 * http get方式调用服务
	 * @author 黄永丰
	 * @createtime 2015年7月21日
	 * @param serviceName 服务名称
	 * @param method 接口方法名称
	 * @param data 传入数据
	 * @return 调用完接口数据
	 */
	public static String httpGet(String data)
	{
		String result = "";
		try
		{
			String openApiUrl = "http://192.168.87.89:8068/openapi?"+data;
			result = HttpsUtil.get(openApiUrl);
		}
		catch (Exception e)
		{
			result = e.getMessage();
		}
		return result;
	}
	
	/**
	 * http post方式调用服务
	 * @author 黄永丰
	 * @createtime 2015年7月21日
	 * @param serviceName 服务名称
	 * @param method 接口方法名称
	 * @param data 传入数据
	 * @return 调用完接口数据
	 */
	public static String httpPost(String data)
	{
		String result = "";
		try
		{
			String openApiUrl = "http://192.168.87.89:8068/openapi";
			result = HttpsUtil.post(openApiUrl, data);
		}
		catch (Exception e)
		{
			result = e.getMessage();
		}
		return result;
	}
	
	/**
	 * https get方式调用服务
	 * @author 黄永丰
	 * @createtime 2015年7月21日
	 * @param serviceName 服务名称
	 * @param method 接口方法名称
	 * @param data 传入数据
	 * @return 调用完接口数据
	 */
	public static String httpSSLGet(String data)
	{
		String result = "";
		try
		{
			String openApiUrl = "https://192.168.87.89:8068/openapi?"+data;
			result = HttpsUtil.sslGet(openApiUrl, "UTF-8");
		}
		catch (Exception e)
		{
			result = e.getMessage();
		}
		return result;
	}
	
	/**
	 * https post方式调用服务
	 * @author 黄永丰
	 * @createtime 2015年7月21日
	 * @param serviceName 服务名称
	 * @param method 接口方法名称
	 * @param data 传入数据
	 * @return 调用完接口数据
	 */
	public static String httpSSLPost(String data)
	{
		String result = "";
		try
		{
			String openApiUrl = "https://192.168.87.89:8068/openapi";
			result = HttpsUtil.sslPost(openApiUrl, data, "UTF-8");
		}
		catch (Exception e)
		{
			result = e.getMessage();
		}
		return result;
	}
	
	
}
