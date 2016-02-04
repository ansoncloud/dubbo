package com.hyf.http.util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GlobalContainer
{

	static ClassPathXmlApplicationContext applicationContext = null;

	public static ClassPathXmlApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	public static void setApplicationContext(ClassPathXmlApplicationContext applicationContext)
	{
		GlobalContainer.applicationContext = applicationContext;
	}
}
