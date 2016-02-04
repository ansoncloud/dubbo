package com.hyf.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.hyf.entity.ResultStruct;


public class MyException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 参数检查统一异常
	 * @param ret 错误码
	 * @param message 错误信息
	 */
	public MyException(int ret, String message)
	{
		//返回错误信息
		new ResultStruct(ret, message);
		// 记录错误日志
		Logger logger = Logger.getLogger(this.getClass());
		//记录第一次错误
		logger.error(message+"\t");
	}
	

	
	/**
	 * 业务处理统一异常
	 * @param ret 错误码
	 * @param message 错误信息
	 * @param e 异常对象e
	 */
	public MyException(int ret, String message,Throwable e)
	{
		super(message, e);
		//返回错误信息
		StackTraceElement[] elems = e.getStackTrace();
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		StringBuffer sb = new StringBuffer();
		sb.append(message);
		StackTraceElement elem = elems[0];
		sb.append("在["+date+"]时间,");
		sb.append("类[" + elem.getClassName() + "]调用["+elem.getMethodName()+"]方法时,");
		sb.append("发生了["+e.getClass().getName()+"]异常,");
		sb.append("异常出现在第[" + elem.getLineNumber()+ "]行代码.");
		// 记录错误日志
		Logger logger = Logger.getLogger(this.getClass());
		//记录第一次错误
		logger.error(sb.toString()+"\t");
		new ResultStruct(ret, message);
	}

	
	
}
