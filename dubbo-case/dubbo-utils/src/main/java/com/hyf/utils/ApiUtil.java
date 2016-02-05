package com.hyf.utils;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;

import com.hyf.global.GlobalContainer;
import com.hyf.openapi.interfaces.ApiInterface;
import com.hyf.security.AesEncryption;
import com.hyf.security.MD5Util;

public class ApiUtil
{
	public final static String PROVIDER_SERVICE = "providerService";
	public final static String CUSTOMER_SERVICE = "customerService";

	/**
	 * dubbo方式调用服务
	 * @author 黄永丰
	 * @createtime 2016年2月1日
	 * @param serviceName 服务名称
	 * @param interfaceName 接口名称
	 * @param data 传入数据
	 * @return 调用完接口数据String
	 */
	public static String dubbo(String serviceName,String interfaceName,String data)
	{
			ApplicationContext ctx = GlobalContainer.getApplicationContext();
			ApiInterface service = (ApiInterface) ctx.getBean(serviceName);
			JSONObject obj = new JSONObject();
			obj.put("interfaceName",interfaceName);
			obj.put("data", data);
			String result = service.doPost(obj.toString());
			return result;
	}

	/**
	 * http post方式调用服务
	 * @author 黄永丰
	 * @createtime 2015年7月21日
	 * @param serviceName 服务名称
	 * @param method 接口方法名称
	 * @param data 传入数据
	 * @return 调用完接口数据
	 */
	public static String httpPost(String url,String data)
	{
		String result = "";
		try
		{
			String appKey = "";//授权给应用的key
			String appSecret = "";//授权给应用的密钥
			String openApiUrl = "";
			String format = "json";
			String aesdata = AesEncryption.encrypt(data, appSecret);//采用AES加密数据
			String interfaceName = "";
			String version = "1.0";//版本
			String timestamp = String.valueOf(System.currentTimeMillis());
			String session = "";
			//签名(用md5加密 appKey + interfaceName + format + aesdata + version + timestamp + session + appKey)
			String sign = MD5Util.getMD5(appKey + interfaceName + format + aesdata + version + timestamp + session + appSecret);
			String param = "appKey=" + appKey + "&interfaceName=" + interfaceName + "&format=" + format + "&data=" + aesdata + "&version=" + version + "&timestamp=" + timestamp + "&session=" + session + "&sign=" + sign;
			result = HttpsUtil.post(openApiUrl, param);
		}
		catch (Exception e)
		{
			result = e.getMessage();
		}
		return result;
	}
	
	/**
	 * http get方式调用服务
	 * @author 黄永丰
	 * @createtime 2015年7月21日
	 * @param serviceName 服务名称
	 * @param method 接口方法名称
	 * @param data 传入数据
	 * @return 调用完接口数据
	 */
	public static String httpGet(String serviceName,String interfaceName,String data)
	{
			
			return "";
	}
	
	
	
}
