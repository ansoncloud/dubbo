package com.hyf.entity;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.hyf.utils.IJsonConfig;

public class RetStruct
{
	private Map<String, Object> retMap;

	public RetStruct()
	{
		retMap = new HashMap<String, Object>();
		retMap.put("ret", 0);
		retMap.put("message", "");
	}

	public RetStruct(String message)
	{
		retMap = new HashMap<String, Object>();
		retMap.put("ret", 0);
		retMap.put("message", message);
	}

	public RetStruct(String message, String key, Object value)
	{
		this(message);
		retMap.put(key, value);
	}

	public RetStruct(String message, Map<String, Object> map)
	{
		retMap = map;
		retMap.put("ret", 0);
		retMap.put("message", message);
	}

	public RetStruct(int ret, String message)
	{
		retMap = new HashMap<String, Object>();
		retMap.put("ret", ret);
		retMap.put("message", message);
	}

	public RetStruct setMessage(String message)
	{
		retMap.put("message", message);
		return this;
	}

	public RetStruct put(String key, Object value)
	{
		retMap.put(key, value);
		return this;
	}

	public RetStruct put(Map<String, Object> map)
	{
		retMap.putAll(map);
		return this;
	}

	@Override
	public String toString()
	{
		String result = JSONObject.fromObject(retMap,IJsonConfig.getInstance()).toString();
		return result;
	}
}
