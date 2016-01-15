package com.hyf.openapi.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.hyf.exception.MyException;
import com.hyf.utils.ObjectParser;

/**
 * validate json example(用来过滤客户端传入的参数,如果json文件里没有写的参数,就不会传入到后台代码里)
 * {
 *  get: {							//第一层 key:接口名称，value: 接口需要传入的json参数格式对象
 * 	 companyid: "Long",				//key:字段名，value:类型名称
 *   consignnotenum(option): "String",	//(option)表示此字段为可选字段
 *   amount: "Double",
 *   state: "Short",
 *   flag: "Integer",
 *   createdtime: "Date",
 *   array: "String[]",				//在类型后再加上"[]"表示传进的"array"为数组
 *   consignnotedetail:{
 *    ...
 *   },
 *   charge:[
 *   	{...},
 *   	{...}
 *   ]
 *  } 				
 * }
 */
public class DataValidator
{
	private Map<String, JsonObject> validateMap;
	private Gson gson;

	public DataValidator()
	{
		validateMap = new HashMap<String, JsonObject>();
		gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

	/**
	 * 获取参数的json校验数据
	 * @author 黄永丰
	 * @createtime 2016年1月2日
	 * @param validateObject 接口服务对象
	 * @param methodName 方法名称
	 * @param data 传入参数 数据
	 * @return 返回json校验后的参数数据
	 * @throws Exception
	 */
	public String getValidData(Object validateObject, String methodName, String data) throws Exception
	{
		try
		{
			// 1、找校验文件
			JsonObject classJson = getValidateMap(validateObject);
			// 1.1、没有校验文件，不需要校验
			if (classJson == null || !classJson.has(methodName)) return data;
			// 1.2、找到校验文件
			// 2、获取当前方法的json文件的数据，并解析
			JsonElement methodJson = classJson.get(methodName);
			// 2、当前方法的json文件的数据解析为空就直接返回当前数据
			if (data.isEmpty() && (methodJson.isJsonNull() || methodJson.toString().equals("[]") || methodJson.toString().equals("{}"))) return data;
			// 2、当前方法的json文件的数据不是json数组对象和json对象，也就是数组 对象
			if (!methodJson.isJsonArray() && !methodJson.isJsonObject())
			{
				String value = methodJson.getAsString().toLowerCase();
				String valueName = value.replace("[]", "");
				if (!value.equals(valueName))
				{
					JsonElement dataElement = gson.fromJson(data, JsonElement.class);
					if (!dataElement.isJsonArray())
					{
						throw new Exception(gson.toJson(dataElement) + "不是数组");
					}
					JsonArray validArray = new JsonArray();
					JsonArray dataArray = dataElement.getAsJsonArray();
					for (JsonElement e : dataArray)
					{
						validArray.add(validateElementType(e, valueName));
					}
					return gson.toJson(validArray);
				}
				else
				{
					return gson.toJson(validateElementType(new JsonPrimitive(data), valueName));
				}
			}
			// 3、
			JsonElement dataJson = gson.fromJson(data, JsonElement.class);
			JsonElement result = getValidJson(methodJson, dataJson);
			return gson.toJson(result);
		}
		catch (Exception e)
		{
			throw new MyException(11, "数据校验失败." + e.getMessage());
		}
	}

	/**
	 * 传入参数与json文件里的参数查检过滤并返回查检过滤后的正确参数
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param validateJson 传入参数
	 * @param dataJson json文件里的参数
	 * @return 返回查检过滤后的正确参数
	 * @throws Exception
	 */
	private JsonElement getValidJson(JsonElement validateJson, JsonElement dataJson) throws Exception
	{
		if (dataJson.isJsonArray() && validateJson.isJsonArray())
		{
			JsonArray result = new JsonArray();
			JsonArray dataArray = dataJson.getAsJsonArray();
			JsonElement validateElement = validateJson.getAsJsonArray().get(0);
			for (int i = 0; i < dataArray.size(); ++i)
			{
				JsonElement e = dataArray.get(i);
				JsonElement r = getValidJson(validateElement, e);
				result.add(r);
			}
			return result;
		}
		else if (dataJson.isJsonObject() && validateJson.isJsonObject())
		{
			JsonObject result = new JsonObject();
			JsonObject dataObject = dataJson.getAsJsonObject();
			JsonObject validateObject = validateJson.getAsJsonObject();
			for (Entry<String, JsonElement> entry : validateObject.entrySet())
			{
				String key = entry.getKey();
				String keyName = key.replace("(option)", "");
				if (!dataObject.has(keyName))
				{
					if (keyName.equals(key)) throw new MyException(11, "找不到" + keyName + "参数.");
					else continue;// 可选字段
				}
				if (entry.getValue().isJsonArray() || entry.getValue().isJsonObject())
				{
					JsonElement r = getValidJson(entry.getValue(), dataObject.get(keyName));
					result.add(keyName, r);
					continue;
				}
				String value = entry.getValue().getAsString().toLowerCase();
				String valueName = value.replace("[]", "");
				JsonElement field = dataObject.get(keyName);
				if (!value.equals(valueName))
				{
					JsonArray validArray = new JsonArray();
					JsonArray array = field.getAsJsonArray();
					for (JsonElement e : array)
					{
						validArray.add(validateElementType(e, valueName));
					}
					result.add(keyName, validArray);
				}
				else
				{
					result.add(keyName, validateElementType(field, valueName));
				}
			}
			return result;
		}
		else
		{
			throw new MyException(11, gson.toJson(dataJson) + "数据格式有误.");
		}
	}

	/**
	 * 查检数据类型
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param param json文件参数数据
	 * @param type 传入数据类型
	 * @return 返回对应的数据类型
	 * @throws Exception
	 */
	private JsonElement validateElementType(JsonElement param, String type) throws Exception
	{
		if (type.equals("long"))
		{
			if (param.isJsonNull()) return param;
			return new JsonPrimitive(param.getAsLong());
		}
		else if (type.equals("int") || type.equals("integer"))
		{
			if (param.isJsonNull()) return param;
			return new JsonPrimitive(param.getAsInt());
		}
		else if (type.equals("double"))
		{
			if (param.isJsonNull()) return param;
			return new JsonPrimitive(param.getAsDouble());
		}
		else if (type.equals("date"))
		{
			if (param.isJsonNull()) return param;
			Date date = ObjectParser.toDate(param.getAsString());
			if (date != null) return new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
			else throw new Exception("日期类型错误");
		}
		else if (type.equals("short"))
		{
			if (param.isJsonNull()) return param;
			return new JsonPrimitive(param.getAsShort());
		}
		else
		{
			if (param.isJsonNull()) return param;
			return new JsonPrimitive(param.getAsString());
		}
	}

	/**
	 * 获当前接口的类的方法的json文件数据
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param validateObject 传入的参数数据
	 * @return 当前接口的类的方法的json文件数据
	 * @throws Exception
	 */
	public JsonObject getValidateMap(Object validateObject) throws Exception
	{
		// validateObject:bean class
		String className = null;
		if (validateObject.getClass().getSuperclass() == Object.class) className = validateObject.getClass().getSimpleName();
		else className = validateObject.getClass().getSuperclass().getSimpleName();
		if (validateMap.containsKey(className)) return validateMap.get(className);
		JsonObject result = null;
		InputStream filestream = null;
		filestream = validateObject.getClass().getResourceAsStream(className + ".json");
		if (filestream == null)
		{
			validateMap.put(className, result);// 找不到校验文件, 不需要校验
			return result;
		}
		InputStreamReader reader = new InputStreamReader(filestream);
		result = ((JsonElement) gson.fromJson(new JsonReader(reader), JsonElement.class)).getAsJsonObject();
		filestream.close();
		reader.close();
		validateMap.put(className, result);
		return result;
	}

}
