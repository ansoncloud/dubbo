package com.hyf.openapi.interfaces;


/**
 * dubbo 用来开发服务注册的，就是提供服务的接口
 * @author 黄永丰
 * @createtime 2016年1月1日
 * @version 1.0
 */
public interface ApiInterface
{
	/**
	 * 通过doPost调用 base服务的接口(api工具用)
	 * @param postdata 客户端传入的map集合数据
	 * @return 调用接口后返回的数据
	 */
	public String doPost(String postdata) throws Exception;
	
}
