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
			String className = "";
			String methodName = "";
			String data = postdata.containsKey("data")?ObjectParser.toString(postdata.get("data")):"";
			String interfaceName = postdata.containsKey("interfaceName")?ObjectParser.toString(postdata.get("interfaceName")):"";
			if (interfaceName == null || "".equals(interfaceName))
			{
				throw new MyException(-1, "interfaceName参数为空.");
			}
			className = getClassName(interfaceName);
			methodName = getMethodName(interfaceName);
			methodArgs.setMethodData(data);
			methodArgs.setMethodName(methodName);
			methodArgs.setClassName(className);
			return methodArgs;
		}
		catch (Exception e)
		{
			if (e.equals(MyException.class)) throw e;
			else throw new MyException(11, "检查方法名称、服务名称、参数数据,并返回对象失败." + e.getMessage());
		}
	}

	/**
	 * 获取调用接口类的方法名称
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param interfaceName 接口名称
	 * @return 返回调用接口类的方法名称
	 */
	public static String getMethodName(String interfaceName)
	{
		String methodName = "";
		String[] methodToken = interfaceName.split("\\.");
		int count = 0;
		for (int i = methodToken.length - 1; i >= 3; i--)
		{
			if (count == 0)
			{
				methodName += methodToken[i].toLowerCase();
			}
			else
			{
				methodName += methodToken[i].substring(0, 1).toUpperCase() + methodToken[i].substring(1);
			}
			count++;
		}
		return methodName;
	}

	/**
	 * 获取调用接口的类名称
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param interfaceName 接口名称
	 * @return 返回用接口的类名称
	 */
	public static String getClassName(String interfaceName)
	{
		String className = "";
		String[] methodToken = interfaceName.split("\\.");
		className = methodToken[2].toLowerCase() + "ServiceImpl";
		return className;
	}

	public static void main(String[] args)
	{
		System.out.println(getClassName("maplemart.openapi.test.get"));
		System.out.println(getMethodName("maplemart.openapi.test.get"));
		System.out.println(getClassName("maplemart.openapi.test.all.get"));
		System.out.println(getMethodName("maplemart.openapi.test.all.get"));
	}

}
