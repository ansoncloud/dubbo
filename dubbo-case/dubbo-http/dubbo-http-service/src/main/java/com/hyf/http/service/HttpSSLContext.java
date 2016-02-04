package com.hyf.http.service;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

/**
 * ssl协议数据加载(final类,不能修改)
 * @author 黄永丰
 * @createtime 2016年2月3日
 * @version 1.0
 */
public final class HttpSSLContext
{
	/**ssl*/
	private static final String PROTOCOL = "SSL";
	/**SSLContext对象*/
	private static final SSLContext SSLCONTEXT;
	/**ssl文件*/
	private static String SERVER_KEY_STORE = "httpssl";
	/**ssl文件访问密码*/
	private static String SERVER_KEY_STORE_PASSWORD = "MapleMart";

	static
	{
		try
		{
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(HttpSSLContext.class.getClassLoader().getResourceAsStream(SERVER_KEY_STORE), SERVER_KEY_STORE_PASSWORD.toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());
			SSLCONTEXT = SSLContext.getInstance(PROTOCOL);
			SSLCONTEXT.init(kmf.getKeyManagers(), null, null);
		}
		catch (Exception e)
		{
			throw new RuntimeException("初始化SSLContext对象失败."+e.getMessage(),e);
		}
	}

	public static SSLContext getSSLContext()
	{
		return SSLCONTEXT;
	}

	private HttpSSLContext()
	{
		// Unused
	}
}
