package com.hyf.security;

import java.security.MessageDigest;

/**
 * MD5算法工具类
 * @author 黄永丰
 * @createtime 2016年2月2日
 * @version 1.0
 */
public class MD5Util
{
	
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
			char[] charArray = str.toCharArray();
			byte[] byteArray = new byte[charArray.length];

			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];
			byte[] md5Bytes = md.digest(byteArray);
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++)
			{
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		}
		catch (Exception e)
		{
			throw new RuntimeException("获取MD5值失败.");
		}
	}
	

	public static void main(String[] args)
	{
		System.out.println(MD5Util.getMD5("透气dfasdf324"));
	}
	
	
}
