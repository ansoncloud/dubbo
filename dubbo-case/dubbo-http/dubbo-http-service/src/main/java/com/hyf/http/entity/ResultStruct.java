package com.hyf.http.entity;



public class ResultStruct
{
	private int resultcode;
	private String resultdesc;
	
	public ResultStruct(int resultcode, String resultdesc)
	{
		super();
		this.resultcode = resultcode;
		this.resultdesc = resultdesc;
	}
	
	public void setResultcode(int resultcode)
	{
		this.resultcode = resultcode;
	}
	public void setResultdesc(String resultdesc)
	{
		this.resultdesc = resultdesc;
	}
	public int getResultcode()
	{
		return resultcode;
	}
	public String getResultdesc()
	{
		return resultdesc;
	}

	@Override
	public String toString()
	{
		return "{resultcode=" + resultcode + ", resultdesc=" + resultdesc + "}";
	}
	
	
	
}
