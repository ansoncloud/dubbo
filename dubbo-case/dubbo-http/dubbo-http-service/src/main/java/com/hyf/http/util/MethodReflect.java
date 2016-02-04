package com.hyf.http.util;

import java.lang.reflect.Method;

/**
 * 接口方法调用与解析
 * @author 黄永丰
 * @createtime 2016年1月2日
 * @version 1.0
 */
public class MethodReflect
{

	/**
	 * 通过反射调用方法
	 * @author 黄永丰
	 * @createtime 2016年1月2日
	 * @param owner 接口服务对象
	 * @param methodName 接口方法名
	 * @param args 调用接口方法的参数数组，方法参数 为1就只传入一个参数 
	 * @return 返回接口方法的返回值 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Object invokeMethod(Object classObject, String methodName, Object[] args)
	{
		Method method = null;
		Object result = null;
		try
		{
			// 1、先获取对象所属的类
			Class ownerClass = classObject.getClass();
			// 2、获取需要调用的方法
			for (Method m : ownerClass.getDeclaredMethods())
			{
				if (m.getName().equalsIgnoreCase(methodName))
				{
					method = m;
					break;
				}
			}
			// 3、调用该方法
			result = method.invoke(classObject, args);
			return result;
		}
		catch (Exception e)
		{
			throw new RuntimeException("调用"+method+"方法."+e.getMessage(),e);
		}
	}

}
