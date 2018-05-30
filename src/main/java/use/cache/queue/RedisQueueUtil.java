package use.cache.queue;

import redis.clients.jedis.Jedis;
import use.cache.com.RedisCacheUtil;
import use.cache.queue.sub.RedisSubscribeBean;
import use.cache.redis.RedisObjectSerial;
import use.common.Eventloop.IEventLoopHandle;

@SuppressWarnings("rawtypes")
public class RedisQueueUtil 
{
	public static void setSub(Class c , String key)
	{
		Jedis j = null;
		try
		{
			j = RedisCacheUtil.getMem().get();
			j.subscribe(new RedisSubscribeBean(c), key);
		}finally
		{
			RedisCacheUtil.getMem().free(j);
		}
	}
	public static void setSub(IEventLoopHandle obj , String key)
	{
		Jedis j = null;
		try
		{
			j = RedisCacheUtil.getMem().get();
			j.subscribe(new RedisSubscribeBean(obj), key);
		}finally
		{
			RedisCacheUtil.getMem().free(j);
		}
	}
	public static void publish(String key , Object obj) throws Exception
	{
		Jedis j = null;
		try
		{
			j = RedisCacheUtil.getMem().get();
			j.publish(key, setValue(obj));
		}finally
		{
			RedisCacheUtil.getMem().free(j);
		}
	}
	
	private static String setValue(Object obj) throws Exception
	{
		try{
			if(obj instanceof String)
			{
				return (String) obj;
			}
			byte[] b = RedisObjectSerial.serialize(obj);
			return new String(b, "utf-8");
		}catch(Exception e)
		{
			throw e;
		}
	}
}
