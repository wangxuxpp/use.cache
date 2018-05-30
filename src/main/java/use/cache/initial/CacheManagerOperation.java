package use.cache.initial;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;

import use.cache.com.RedisCacheUtil;
import use.cache.springCache.RedisCacheCore;

public class CacheManagerOperation {

	protected final static Logger log = LoggerFactory.getLogger(CacheManagerOperation.class);
	private static SimpleCacheManager fCache = null;
	
	public static final String defaultCache = "default";
	public static final String secondCache = "redisDataBaseSecondName";
	public static final String limitsCache = "limitsCache";
	
	public static SimpleCacheManager getCacheManager()
	{
		log.info("系统缓存 ----initial start");
		if (fCache == null)
		{
			fCache = new SimpleCacheManager();
		} else {
			return fCache;
		}
		List<Cache> l = new ArrayList<Cache>();
		if (RedisCacheUtil.getParameter().isRedisEanble())
		{
			RedisCacheCore c = new RedisCacheCore(CacheManagerOperation.defaultCache);
			l.add(c);
			RedisCacheCore d = new RedisCacheCore(RedisCacheUtil.getMem1() , CacheManagerOperation.secondCache);
			d.setExpire(3600);
			l.add(d);
			RedisCacheCore e = new RedisCacheCore(RedisCacheUtil.getMem2() , CacheManagerOperation.limitsCache);
			l.add(e);
			log.info("系统缓存类型： redis");
		} else {
			/*ConcurrentMapCacheFactoryBean c = new ConcurrentMapCacheFactoryBean();
			c.setBeanName(CacheManagerOperation.cacheName);
			c.setName(CacheManagerOperation.cacheName);*/
			ConcurrentMapCache c =new ConcurrentMapCache(CacheManagerOperation.defaultCache);
		    l.add(c);
		    ConcurrentMapCache d =new ConcurrentMapCache(CacheManagerOperation.secondCache);
		    l.add(d);
		    ConcurrentMapCache e =new ConcurrentMapCache(CacheManagerOperation.limitsCache);
		    l.add(e);
		    log.info("系统缓存类型： map");
		}
		fCache.setCaches(l);
		log.info("系统缓存 ----initial end");
		return fCache;
	}
}
