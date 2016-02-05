package com.hyf.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * http请求工具类
 * @author 黄永丰
 * @createtime 2016年2月2日
 * @version 1.0
 */
public class HttpsUtil
{

	private static class TrustAnyTrustManager implements X509TrustManager
	{
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
		}
		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier
	{
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	}

	/**
	 * 向指定URL发送POST方法的请求 (Https 模式)
	 * @param url 发送请求的URL
	 * @param content 发送的数据 请求参数应该是 name1=value1&name2=value2 的形式。
	 * @param charset 字符集
	 * @return 所代表远程资源的响应结果
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static String sslPost(String url, String content, String charset) throws NoSuchAlgorithmException, KeyManagementException, IOException
	{
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
		URL console = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.connect();
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(content.getBytes(charset));
		// flush输出流的缓冲
		out.flush();
		out.close();
		InputStream is = conn.getInputStream();
		if (is != null)
		{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1)
			{
				outStream.write(buffer, 0, len);
			}
			is.close();
			return new String(outStream.toByteArray(),charset);
		}
		return null;
	}

	/**
	 * 向指定URL发送GET方法的请求 (Https 模式)
	 * @param url 发送请求的URL 包含请求参数，请求参数应该是 url?name1=value1&name2=value2 的形式。
	 * @param charset 字符集
	 * @return 所代表远程资源的响应结果
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static String sslGet(String url, String charset) throws NoSuchAlgorithmException, KeyManagementException, IOException
	{
		InputStream is = null;
		try
		{
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.connect();
			is = conn.getInputStream();
			if (is != null)
			{
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1)
				{
					outStream.write(buffer, 0, len);
				}
				try
				{
					return new String(outStream.toByteArray(),charset);
				}
				catch (Exception e)
				{
					throw new RuntimeException("发送GET请求出现异常."+e.getMessage());
				}
				finally
				{
					is.close();
					is = null;
					outStream.close();
					outStream = null;
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("发送sslGet请求出现异常."+e.getMessage());
		}
		finally
		{
			if (is != null)
				is.close();
		}
		return null;
	}

	/**
	 * 向指定URL发送GET方法的请求 在代码里会拼成url?name1=value1&name2=value2
	 * @param url 发送请求的URL
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String get(String url)
	{
		String result = "";
		BufferedReader in = null;
		try
		{
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
//			Map<String, List<String>> map = connection.getHeaderFields();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += line;
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("发送GET请求出现异常."+e.getMessage());
		}
		// 使用finally块来关闭输入流
		finally
		{
			try
			{
				if (in != null)
					in.close();
			}
			catch (Exception ex)
			{
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String post(String url, String param)
	{
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try
		{
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null)
			{
				result += line;
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("发送POST请求出现异常."+e.getMessage());
		}
		// 使用finally块来关闭输出流、输入流
		finally
		{
			try
			{
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			}
			catch (IOException ex)
			{
			}
		}
		return result;
	}
	
}
