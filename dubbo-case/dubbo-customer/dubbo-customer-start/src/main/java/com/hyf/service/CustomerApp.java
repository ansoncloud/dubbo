package com.hyf.service;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hyf.global.GlobalContainer;

/**
 * 服务启动
 * @author 黄永丰
 * @createtime 2016年1月3日
 * @version 1.0
 */
public class CustomerApp 
{
	static Log log = LogFactory.getLog(CustomerApp.class);  
	
	public static void main(String[] args)
	{
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		applicationContext.start();
		GlobalContainer.setApplicationContext(applicationContext);
		log.info("---------------start dubbo-customer-business sucessful---------------------");
		try
		{
			System.in.read();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
