package com.hyf.entity;

public class RetResponse
{
	private int ret;
	private String message;

	public RetResponse(int ret, String message)
	{
		this.ret = ret;
		this.message = message;
	}

	public RetResponse()
	{
	}

	public int getRet()
	{
		return ret;
	}

	public void setRet(int ret)
	{
		this.ret = ret;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
