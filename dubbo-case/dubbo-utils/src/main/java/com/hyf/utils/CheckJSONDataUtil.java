package com.hyf.utils;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.hyf.exception.MyException;

/**
 * service层传入JSON格式字符串校验与转换成Map集合工具类
 * @author HuangYongFeng
 * @createtime 2015年7月16日
 * @version 1.0
 */
public class CheckJSONDataUtil
{

	/**
	 * 传入参数JSON格式校验与转换
	 * @author HuangYongFeng
	 * @createtime 2015年7月16日
	 * @param data 传入JSON格式实字符串
	 * @return Map<String,Object> 返回JSON转换成Map数据
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> checkJSONData(String data)
	{
		if(data == null || "".equals(data.trim()))
		{
			throw new MyException(10507, "传入的参数data不能为空.");
		}
		Map<String,Object> dataMap = new HashMap<String,Object>();
		try
		{
			dataMap = JSONObject.fromObject(data,IJsonConfig.getInstance());
		}
		catch(Exception e)
		{
			throw new MyException(10508, "解析参数出错,请检查传入参数JSON格式是否正确.");
		}
		return dataMap;
	}
	
	
}
