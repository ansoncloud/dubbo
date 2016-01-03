package com.hyf.openapi.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyf.exception.MyException;
import com.hyf.global.GlobalContainer;
import com.hyf.openapi.entity.MethodArgs;
import com.hyf.openapi.interfaces.ApiInterface;
import com.hyf.openapi.utils.DataValidator;
import com.hyf.openapi.utils.MethodArgsUtil;
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
	public String doPost(Map<String, String> postdata) throws Exception
	{
		try
		{
			// 1、获取方法名称、服务名称、参数 数据的对象
			MethodArgs methodargs = MethodArgsUtil.checkPostMethodData(postdata);
			// 2、获取传入调用接口方法参数的数据
			String validData = methodargs.getMethodData();
			// 3、获取传入调用接口方法参数的数据
			String methodName = methodargs.getMethodData();
			// 4、获取调用服务对象
			Object serviceObject = GlobalContainer.getApplicationContext().getBean(methodargs.getServiceName());
			// 5、数据校验
			String paramsData = dataValidator.getValidData(serviceObject,methodName,validData);
			// 6、调用接口方法
			String result = String.valueOf(MethodReflect.invokeMethod(serviceObject,methodName,new Object[]{paramsData}));
			return result;
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			if (e.equals(MyException.class)) throw e;
			else throw new MyException(11, "调用doPost处理异常."+e.getMessage());
		}
	}
	

}
