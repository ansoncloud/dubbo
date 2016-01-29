package com.hyf.openapi.impl;

import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyf.entity.RetStruct;
import com.hyf.global.GlobalContainer;
import com.hyf.openapi.entity.MethodArgs;
import com.hyf.openapi.interfaces.ApiInterface;
import com.hyf.openapi.utils.DataValidator;
import com.hyf.openapi.utils.MethodArgsUtil;
import com.hyf.utils.CheckJSONDataUtil;
import com.hyf.utils.MethodReflect;

public class ApiInterfaceServiceImpl implements ApiInterface
{

	private static Log log = LogFactory.getLog(ApiInterfaceServiceImpl.class);
	@Autowired
	DataValidator dataValidator;

	/**
	 * 通过doPost调用 base服务的接口(api工具用)
	 * @param postdata 客户端传入的map集合数据
	 * @return 调用接口后返回的数据
	 * @throws Exception 
	 */
	@Override
	public String doPost(String postdata)
	{
		try
		{
			// 1、获取方法名称、服务名称、参数 数据的对象
			Map<String,Object> dataMap = CheckJSONDataUtil.checkJSONData(postdata);
			MethodArgs methodargs = MethodArgsUtil.checkPostMethodData(dataMap);
			// 2、获取传入调用接口方法参数的数据
			String validData = methodargs.getMethodData();
			// 3、获取传入调用接口的类名称
			String className = methodargs.getClassName();
			// 4、获取传入调用接口方法名称
			String methodName = methodargs.getMethodName();
			// 5、获取调用类对象
			Object classObject = GlobalContainer.getApplicationContext().getBean(className);
			// 6、数据校验
			String paramsData = dataValidator.getValidData(classObject,methodName,validData);
			// 7、调用接口方法
			String result = String.valueOf(MethodReflect.invokeMethod(classObject,methodName,new Object[]{paramsData/**调用方法参数多少个这里数组就是多少个长度数组*/}));
			return result;
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			return new RetStruct(-1,"调用doPost方法处理异常."+e.getMessage()).toString();
		}
	}
	

}
