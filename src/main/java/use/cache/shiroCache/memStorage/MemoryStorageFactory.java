package use.cache.shiroCache.memStorage;

import java.util.concurrent.ConcurrentMap;

import use.cache.redis.RedisShareMemory;

public class MemoryStorageFactory 
{

	public static IMemoryStorage getMemoryStorage(org.springframework.cache.Cache cache)
	{
		IMemoryStorage bean = null;
		Object cacheObject = cache.getNativeCache();
		if(cacheObject instanceof RedisShareMemory)
		{
			bean = new MemoryStorageRedis();	
		}
		if(cache.getNativeCache() instanceof ConcurrentMap)
		{
			bean = new MemoryStorageMap();	
		}
		if(bean == null)
		{
			return null;
		}
		bean.setSpringCache(cacheObject);
		return bean;
	}
}
