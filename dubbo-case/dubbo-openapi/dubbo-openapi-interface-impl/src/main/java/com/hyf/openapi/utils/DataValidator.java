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
	public String getValidData(Object classObject, String methodName, String data) throws Exception
	{
		try
		{
			// 1、找校验文件,并返回文件内容的json格式数据
			JsonObject classJson = getValidateMap(classObject);
			// 1.1、没有校验文件，不需要校验
			if (classJson == null || !classJson.has(methodName)) return data;
			// 1.2、找到校验文件
			// 2、获取当前方法的json文件的数据，并解析
			JsonElement methodJson = classJson.get(methodName);
			// 2、当前方法的json文件的数据解析为空或对应json文件里方法是[]或{}这样空字段的就直接返回当前数据
			if (data.isEmpty() && (methodJson.isJsonNull() || methodJson.toString().equals("[]") || methodJson.toString().equals("{}"))) return data;
			// 2、当前方法的json文件的数据不是json数组对象和json对象,直接抛错
			boolean jsonArr = methodJson.isJsonArray();
			boolean jsonObj = methodJson.isJsonObject();
			if (!jsonArr && !jsonObj)
			{
				throw new Exception(classObject.getClass().getName()+".json文件里的"+methodName+"方法格式有误.");
			}
			// 3、传入数据转换成json对象，检验传入的参数与json文件里的参数做过滤
			JsonElement dataJson = gson.fromJson(data, JsonElement.class);
			JsonElement result = getValidJson(methodJson, dataJson);
			return gson.toJson(result);
		}
		catch (Exception e)
		{
			throw new MyException(-1, "数据校验失败." + e.getMessage());
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
		//数组对象
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
		//json对象
		else if (dataJson.isJsonObject() && validateJson.isJsonObject())
		{
			JsonObject result = new JsonObject();
			JsonObject dataObject = dataJson.getAsJsonObject();
			JsonObject validateObject = validateJson.getAsJsonObject();
			//1、遍历json文件里的数据
			for (Entry<String, JsonElement> entry : validateObject.entrySet())
			{
				String key = entry.getKey();//有两种如 id 或id(option)
				String keyName = key.replace("(option)", "");//去掉json里有可选参数(option)标识
				//2、判断传入参数里的是否包含json文件里当前的字段值,如果没有再判断是否是可选参数
				if (!dataObject.has(keyName))
				{
					//json里原来的值id与去掉option里的值是一样，说明是必填参数，就要抛出异常，否则遍历下个参数
					if (keyName.equals(key)) throw new MyException(-1,"找不到"+keyName+"参数.");
					else continue;// 可选字段
				}
				//3、如果json文件里的参数里的值json对象或数组对象，再递归做遍历里面参数
				if (entry.getValue().isJsonArray() || entry.getValue().isJsonObject())
				{
					//调回现在这个方法做校验
					JsonElement r = getValidJson(entry.getValue(), dataObject.get(keyName));
					result.add(keyName, r);
					continue;
				}
				//4、获取json文件里参数的类型
				String value = entry.getValue().getAsString().toLowerCase();
				//如果是数组类型，去掉[]符号
				String valueName = value.replace("[]", "");
				//获取传入参数里的值
				JsonElement field = dataObject.get(keyName);
				//如果json文件里参数的类型与去掉[]符号的值不一样,说明是数组类型参数
				if (!value.equals(valueName))
				{
					JsonArray validArray = new JsonArray();
					JsonArray array = field.getAsJsonArray();
					for (JsonElement e : array)
					{
						validArray.add(validateElementType(e, valueName));//查检数据类型
					}
					result.add(keyName, validArray);
				}
				else
				{
					result.add(keyName, validateElementType(field, valueName));//查检数据类型
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
	 * 查检数据类型,并转换返回对应类型的值
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
	public JsonObject getValidateMap(Object classObject) throws Exception
	{
		//1、找出当前类对象的类名称
		String className = null;
		if (classObject.getClass().getSuperclass() == Object.class) 
			className = classObject.getClass().getSimpleName();
		else 
			className = classObject.getClass().getSuperclass().getSimpleName();
		//2、如果校验map集合里已有了,就直接返回，不用解析json文件
		if (validateMap.containsKey(className)) 
			return validateMap.get(className);
		//3、校验map集合里已没有当前类的json文件，就解析json文件内容
		JsonObject result = null;
		InputStream filestream = null;
		filestream = classObject.getClass().getResourceAsStream(className + ".json");
		// 3.1找不到校验文件, 不需要校验
		if (filestream == null)
		{
			validateMap.put(className, result);
			return result;
		}
		//3.2找到json文件，解析json文件内容
		InputStreamReader reader = new InputStreamReader(filestream);
		result = ((JsonElement) gson.fromJson(new JsonReader(reader), JsonElement.class)).getAsJsonObject();
		filestream.close();
		reader.close();
		//4、放到map集合里，下次可以不用再找多一次
		validateMap.put(className, result);
		//返回json文件里内容的json格式数据
		return result;
	}

}
