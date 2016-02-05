package com.hyf.entity;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.hyf.utils.IJsonConfig;

public class ResultStruct
{
	private Map<String, Object> retMap;

	public ResultStruct()
	{
		retMap = new HashMap<String, Object>();
		retMap.put("resultcode", 0);
		retMap.put("resultdesc", "success");
	}

	public ResultStruct(String resultdesc)
	{
		retMap = new HashMap<String, Object>();
		retMap.put("resultcode", 0);
		retMap.put("resultdesc", resultdesc);
	}

	public ResultStruct(String resultdesc, String key, Object value)
	{
		retMap = new HashMap<String, Object>();
		retMap.put("resultcode", 0);
		retMap.put("resultdesc", resultdesc);
		retMap.put(key, value);
	}

	public ResultStruct(String resultdesc, Map<String, Object> map)
	{
		retMap = map;
		retMap.put("resultcode", 0);
		retMap.put("resultdesc", resultdesc);
	}

	public ResultStruct(int resultcode, String resultdesc)
	{
		retMap = new HashMap<String, Object>();
		retMap.put("resultcode", resultcode);
		retMap.put("resultdesc", resultdesc);
	}

	@Override
	public String toString()
	{
		String result = JSONObject.fromObject(retMap,IJsonConfig.getInstance()).toString();
		return result;
	}
	
}
