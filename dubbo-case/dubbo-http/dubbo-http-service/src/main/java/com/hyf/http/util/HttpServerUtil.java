package com.hyf.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * httpserver服务的一些配置参数
 * @author 黄永丰
 * @createtime 2016年2月3日
 * @version 1.0
 */
public class HttpServerUtil
{
	/**开启的服务端口*/
	public static final int PORT;
	/**是否开启http协议的Keep-Alive*/
	public static final boolean KEEP_ALIVE;
	/**是否支持ssl连接*/
	public static final boolean SSL;
	/**是否双向加密*/
	public static final boolean WAY_ENCRYPTION;
	/**数据读入超时时间*/
	public static final int READTIME;
	/**输入写入超时时间*/
	public static final int WRITETIME;
	/**aggregator*/
	public static final int AGGREGATOR;
	
	static
	{
		try
		{
			InputStream is = HttpServerUtil.class.getResourceAsStream("/httpserver.properties");
			Properties pro = new Properties();
			pro.load(is);
			PORT = Integer.parseInt(pro.getProperty("httpserver.port"));
			KEEP_ALIVE = Boolean.parseBoolean(pro.getProperty("httpserver.keep_alive"));
			SSL = Boolean.parseBoolean(pro.getProperty("httpserver.ssl"));
			WAY_ENCRYPTION = Boolean.parseBoolean(pro.getProperty("httpserver.way_encryption"));
			READTIME = Integer.parseInt(pro.getProperty("httpserver.readtime"));
			WRITETIME = Integer.parseInt(pro.getProperty("httpserver.writetime"));
			AGGREGATOR = Integer.parseInt(pro.getProperty("httpserver.aggregator"));
		}
		catch (IOException e)
		{
			throw new RuntimeException("加载httpserver配置文件失败."+e.getMessage(),e);
		}
	}
	
}

