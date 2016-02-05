package com.hyf.business.test.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyf.entity.ResultStruct;
import com.hyf.security.AesEncryption;
import com.hyf.utils.ApiUtil;
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
	 * @interfaceName maplemart.customer.test.get
	 * @author 黄永丰
	 * @createtime 2016年1月3日
	 * @param data {"id":xx,"name":xx}
	 * @return {ret: 0,message:"调用service服务成功.","data":{"total":xx,"rows":{"get":xx}}}
	 * @throws Exception
	 */
	public String get(String data) throws Exception
	{
		Map<String,Object> dataMap = CheckJSONDataUtil.checkJSONData(data);
//		ApiInterface service = (ApiInterface) GlobalContainer.getApplicationContext().getBean("providerService");
//		dataMap.put("interfaceName", "maplemart.provider.test.get");
//		String resultdata = service.doPost(JSONObject.fromObject(dataMap).toString());
		String resultdata = ApiUtil.dubbo(ApiUtil.PROVIDER_SERVICE, "maplemart.provider.test.get",data);
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("total", 1);
		result.put("rows",dataMap);
		result.put("resultdata",resultdata);
		return new ResultStruct("消费者customerService服务的提供者TestServiceImpl类的get方法调用成功.","data",result).toString();
	}
}
