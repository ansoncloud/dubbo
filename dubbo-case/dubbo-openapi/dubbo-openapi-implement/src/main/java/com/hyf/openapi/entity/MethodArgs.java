package com.hyf.openapi.entity;

public class MethodArgs
{
	private String serviceName;
	private String methodName;
	private String methodData;

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	public String getMethodData()
	{
		return methodData;
	}

	public void setMethodData(String methodData)
	{
		this.methodData = methodData;
	}

	@Override
	public String toString()
	{
		return "MethodArgs [serviceName=" + serviceName + ", methodName=" + methodName + ", methodData=" + methodData + "]";
	}

}
