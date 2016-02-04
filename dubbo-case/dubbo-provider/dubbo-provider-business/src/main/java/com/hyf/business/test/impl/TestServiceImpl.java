package com.hyf.business.test.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyf.entity.ResultStruct;
import com.hyf.exception.MyException;
import com.hyf.utils.CheckJSONDataUtil;

/**
 * 接口命名规则:(maplemart(公司英文名称).工程名.类名(不加ServiceImpl).方法倒数第一个单词全小写.(按顺序倒数..).方法第一个单词全小写)
 * 如: maplemart.provider.test.get 接口的类名是:TestServiceImpl 方法名是:get
 *    maplemart.provider.test.all.get 接口的类名是:TestServiceImpl 方法名是:getAll
 *    maplemart.provider.test.type.all.get 接口的类名是:TestServiceImpl 方法名是:getAllType)
 * @author 黄永丰
 * @createtime 2015年12月08日
 * @version 1.0
 */
@Service
public class TestServiceImpl
{
	/**
	 * 测试，dubbo接口
	 * @interfaceName maplemart.provider.test.get
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param data {"id":xx,"name":xx}
	 * @return {ret: 0,message:"调用service服务成功.","data":{"total":xx,"rows":{"get":xx}}}
	 * @throws Exception
	 */
	public String get(String data) throws Exception
	{
		Map<String, Object> dataMap = CheckJSONDataUtil.checkJSONData(data);
		if (dataMap.get("id") == null || "".equals(dataMap.get("id")))
		{
			throw new MyException(-1, "参数id不能为空.");
		}
		if (dataMap.get("name") == null || "".equals(dataMap.get("name").toString().trim()))
		{
			throw new MyException(-1, "参数name不能为空.");
		}
		try
		{
			Map<String, Object> result = new HashMap<String,Object>();
			result.put("total", 1);
			result.put("rows", dataMap);
			result.put("time", new Date());
			result.put("time2", new java.sql.Date(System.currentTimeMillis()));
			result.put("time3", new Timestamp(System.currentTimeMillis()));
			return new ResultStruct("调用provider里的接口成功.","data",result).toString();
		}
		catch (Exception e)
		{
			if (e.getClass().equals(MyException.class)) throw e;
			else throw new MyException(-1, "调用provider里的接口失败." + e.getMessage(),e);
		}
	}
	
	
	
	/**
	 * 测试，dubbo接口
	 * @interfaceName maplemart.provider.test.get
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param data {"id":xx,"name":xx,(可选参数)"mail":xx}
	 * @return {ret: 0,message:"调用service服务成功.","data":{"total":xx,"rows":{"get":xx}}}
	 * @throws Exception
	 */
	public String getAll(String data) throws Exception
	{
		Map<String, Object> dataMap = CheckJSONDataUtil.checkJSONData(data);
		if (dataMap.get("id") == null || "".equals(dataMap.get("id")))
		{
			throw new MyException(-1, "参数id不能为空.");
		}
		if (dataMap.get("name") == null || "".equals(dataMap.get("name").toString().trim()))
		{
			throw new MyException(-1, "参数name不能为空.");
		}
		try
		{
			Map<String, Object> result = new HashMap<String,Object>();
			result.put("total", 1);
			result.put("rows", data);
			return new ResultStruct("调用provider里的接口成功.","data",result).toString();
		}
		catch (Exception e)
		{
			if (e.getClass().equals(MyException.class)) throw e;
			else throw new MyException(-1, "调用provider里的接口失败." + e.getMessage(),e);
		}
	}
	
	
	
}
