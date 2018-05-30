package use.cache.com;

import use.cache.redis.RedisShareMemory;

public class RedisCacheUtil {
	
	private static RedisCacheParameter param = null;
	
	private static RedisShareMemory mem = null;
	private static RedisShareMemory mem1 = null;
	private static RedisShareMemory mem2 = null;
	private static RedisShareMemory mem3 = null;
	private static RedisShareMemory mem4 = null;
	private static RedisShareMemory mem5 = null;
	
	public static synchronized RedisCacheParameter getParameter()
	{
		if (param == null)
		{
			param = new RedisCacheParameter();
			param.readParameter("../config/mem.properties");
		}
		return param;
	}
	
	public static RedisShareMemory getMem5()
	{
		if (mem5 == null)
		{
			mem5 = new RedisShareMemory(RedisCacheUtil.getParameter()).setDataBaseIndex(5);
		}	
		return mem5;
	}
	public static RedisShareMemory getMem4()
	{
		if (mem4 == null)
		{
			mem4 = new RedisShareMemory(RedisCacheUtil.getParameter()).setDataBaseIndex(4);
		}	
		return mem4;
	}
	public static RedisShareMemory getMem3()
	{
		if (mem3 == null)
		{
			mem3 = new RedisShareMemory(RedisCacheUtil.getParameter()).setDataBaseIndex(3);
		}	
		return mem3;
	}
	public static RedisShareMemory getMem2()
	{
		if (mem2 == null)
		{
			mem2 = new RedisShareMemory(RedisCacheUtil.getParameter()).setDataBaseIndex(2);
		}	
		return mem2;
	}
	public static RedisShareMemory getMem1()
	{
		if (mem1 == null)
		{
			mem1 = new RedisShareMemory(RedisCacheUtil.getParameter()).setDataBaseIndex(1);
		}	
		return mem1;
	}
	public static RedisShareMemory getMem()
	{
		if (mem == null)
		{
			mem = new RedisShareMemory(RedisCacheUtil.getParameter());
		}	
		return mem;
	}

}
