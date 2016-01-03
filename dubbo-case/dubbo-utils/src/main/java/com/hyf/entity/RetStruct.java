package com.hyf.entity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
		Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.toJson(retMap);
	}
}
