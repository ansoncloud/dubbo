package com.hyf.openapi.entity;

public class MethodArgs
{
	private String className;
	private String methodName;
	private String methodData;

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
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
		return "MethodArgs [className=" + className + ", methodName=" + methodName + ", methodData=" + methodData + "]";
	}

}
