package com.hyf.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES加密工具类
 * @author 黄永丰
 * @createtime 2016年2月2日
 * @version 1.0
 */
public class AesEncryption
{
	/** 加密方式 */
	public static final String KEY_ALGORITHM = "AES";
	/** 加密、解密/ 工作模式/ 填充方式 */
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * AES-128-ECB加密(key需要为16位)
	 * @author 黄永丰
	 * @createtime 2016年2月2日
	 * @param data 要加密的数据
	 * @param key 加密的key(自己定义好的)
	 * @return 返回加密后的字符串
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception
	{
		if (key == null)
		{
			throw new RuntimeException("key为null.");
		}
		// 判断Key是否为16位
		if (key.length() != 16)
		{
			throw new RuntimeException("key长度不是16位.");
		}
		try
		{
			byte[] raw = key.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, AesEncryption.KEY_ALGORITHM);// AES
			Cipher cipher = Cipher.getInstance(AesEncryption.CIPHER_ALGORITHM);// AES/ECB/PKCS5Padding "算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(data.getBytes("utf-8"));
			// 此处使用BASE64做转码功能，同时能起到2次加密的作用。可以返回字符串
			return new Base64().encodeToString(encrypted);
		}
		catch (Exception e)
		{
			throw new RuntimeException("加密失败." + e.getMessage());
		}
	}

	/**
	 * AES-128-ECB解密(key需要为16位)
	 * @author 黄永丰
	 * @createtime 2016年2月2日
	 * @param data 解密的数据
	 * @param key 加密的key(自己定义好的和加密的key要一样)
	 * @return 返回解密后的数据
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws Exception
	{
			if (key == null)
			{
				throw new RuntimeException("key为null.");
			}
			// 判断Key是否为16位
			if (key.length() != 16)
			{
				throw new RuntimeException("key长度不是16位.");
			}
			try
			{
				byte[] raw = key.getBytes("utf-8");
				SecretKeySpec skeySpec = new SecretKeySpec(raw, AesEncryption.KEY_ALGORITHM);// AES
				Cipher cipher = Cipher.getInstance(AesEncryption.CIPHER_ALGORITHM);// AES/ECB/PKCS5Padding "算法/模式/补码方式"
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
				byte[] encrypted1 = new Base64().decode(data);// 先用base64解密
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original, "utf-8");
				return originalString;
		}
		catch (Exception e)
		{
			throw new RuntimeException("解密失败." + e.getMessage());
		}
	}

	
	public static void main(String[] args) throws Exception
	{
		/*
		 * 此处使用AES-128-ECB加密模式，key需要为16位。
		 */
		String key = "1234567890123456";
		// 需要加密的字串
		String data = "数据";
		System.out.println(data);
		// 加密
		String enString = AesEncryption.encrypt(data, key);
		System.out.println("加密后的字串是：" + enString);

		// 解密
		String deString = AesEncryption.decrypt(enString, key);
		System.out.println("解密后的字串是：" + deString);
	}

}
