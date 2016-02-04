package com.hyf.security;

import java.security.MessageDigest;


/**
 * MD5算法工具类
 * @author 黄永丰
 * @createtime 2016年2月2日
 * @version 1.0
 */
public class CopyOfMD5Util
{
	/**设置生成MD5返回包含的字符 */
	private static final String[] chars = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9","a", "b", "c", "d", "e", "f" };
	
	/**
	 * 获取MD5值
	 * @author 黄永丰
	 * @createtime 2016年2月2日
	 * @param str 传入数据
	 * @return 返回生成的MD5值
	 */
	public static String getMD5(String str)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			return byteToString(md.digest(str.getBytes()));
		}
		catch (Exception e)
		{
			throw new RuntimeException("获取MD5值失败.");
		}
	}
	
	/**
	 * 返回形式为数字和字符串(转换字节数组为16进制字串)
	 * @author 黄永丰
	 * @createtime 2016年2月2日
	 * @param bByte 传入的byte数据
	 * @return 返回组装后的数据
	 */
	private static String byteToString(byte[] bByte)
	{
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++)
		{
			sBuffer.append(byteToArrayString(bByte[i]));
//			sBuffer.append(byteToNumber(bByte[i]));
		}
		return sBuffer.toString();
	}
	
	/**
	 * 返回形式为数字和字符串
	 * @author 黄永丰
	 * @createtime 2016年2月2日
	 * @param bByte 传入的byte数据
	 * @return 返回组装后的数据
	 */
	private static String byteToArrayString(byte bByte)
	{
		int iRet = bByte;
		if (iRet < 0)
		{
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return chars[iD1] + chars[iD2];
	}
	
	/**
	 * 返回形式只为数字
	 * @author 黄永丰
	 * @createtime 2016年2月2日
	 * @param bByte 传入的byte数据
	 * @return 返回组装后的数据
	 */
	private static String byteToNumber(byte bByte)
	{
		int iRet = bByte;
		if (iRet < 0)
		{
			iRet += 256;
		}
		return String.valueOf(iRet);
	}
	

	public static void main(String[] args)
	{
		System.out.println(getMD5("32443fdfadfaserefdafasdfa我"));
	}

}
