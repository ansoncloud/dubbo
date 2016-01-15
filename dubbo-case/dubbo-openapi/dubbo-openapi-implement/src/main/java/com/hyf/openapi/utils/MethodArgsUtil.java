package com.hyf.openapi.utils;

import java.util.Map;

import com.hyf.exception.MyException;
import com.hyf.openapi.entity.MethodArgs;
import com.hyf.utils.ObjectParser;

public class MethodArgsUtil
{
	/**
	 * 检查方法名称、服务名称、参数数据，并返回对象
	 * @author 黄永丰
	 * @createtime 2016年1月2日
	 * @param postdata 传入数据
	 * @return 方法名称、服务名称、参数数据对象
	 */
	public static MethodArgs checkPostMethodData(Map<String,Object> postdata) throws Exception
	{
		MethodArgs methodArgs = new MethodArgs();
		try
		{
			String servicename = "";
			String methodname = "";
			String args = "";
			String method = postdata.containsKey("method")?ObjectParser.toString(postdata.get("method")):"";
			if (method == null || "".equals(method))
			{
				throw new MyException(11, "method参数为空.");
			}
			servicename = getServiceName(method);
			methodname = getMethodName(method);
			methodArgs.setMethodData(args);
			methodArgs.setMethodName(methodname);
			methodArgs.setServiceName(servicename);
			return methodArgs;
		}
		catch (Exception e)
		{
			if (e.equals(MyException.class)) throw e;
			else throw new MyException(11, "检查方法名称、服务名称、参数数据，并返回对象失败." + e.getMessage());
		}
	}

	/**
	 * 获取调用接口类的方法名称
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param method 接口名称
	 * @return 返回调用接口类的方法名称
	 */
	public static String getMethodName(String method)
	{
		String methodname = "";
		String[] methodToken = method.split("\\.");
		int count = 0;
		for (int i = methodToken.length - 1; i >= 3; i--)
		{
			if (count == 0)
			{
				methodname = methodname + methodToken[i].toLowerCase();
			}
			else
			{
				methodname = methodname + methodToken[i].substring(0, 1).toUpperCase() + methodToken[i].substring(1);
			}
			count++;
		}
		return methodname;
	}

	/**
	 * 获取调用接口的类名称
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param method 接口名称
	 * @return 返回用接口的类名称
	 */
	public static String getServiceName(String method)
	{
		String servicename = "";
		String[] methodToken = method.split("\\.");
		servicename = methodToken[2].toLowerCase() + "ServiceImpl";
		return servicename;
	}

	public static void main(String[] args)
	{
		System.out.println(getServiceName("maplemart.openapi.test.get"));
		System.out.println(getMethodName("maplemart.openapi.test.get"));
		System.out.println(getServiceName("maplemart.openapi.test.all.get"));
		System.out.println(getMethodName("maplemart.openapi.test.all.get"));
	}

}
