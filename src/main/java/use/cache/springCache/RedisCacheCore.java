package use.cache.springCache;

import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import redis.clients.jedis.Jedis;
import use.cache.com.RedisCacheUtil;
import use.cache.com.except.RedisReadException;
import use.cache.com.except.RedisWriteException;
import use.cache.redis.RedisShareMemory;

public class RedisCacheCore implements Cache{
	
	private RedisShareMemory redisMem = null;
	
	public RedisCacheCore()
	{
		this(null , "");
	}
	public RedisCacheCore(String value)
	{
		this(null , value);
	}
	public RedisCacheCore(RedisShareMemory mem ,String value)
	{
		name = value;
		if(mem == null)
		{
			redisMem = RedisCacheUtil.getMem();
		}else {
			redisMem = mem;
		}
	}
	
	private String name = "";
	
	public void setName(String name) {
		this.name = name;
	}
	//清空缓存  
	public void clear() {
		// TODO Auto-generated method stub
		Jedis r = null;
		try
		{
			r = redisMem.get();
			r.flushDB();
		}
		finally
		{
			redisMem.free(r);
		}
		
	}
	//从缓存中移除key对应的缓存 
	public void evict(Object key)
	{
		redisMem.del(key);
	}
	//根据key得到一个ValueWrapper，然后调用其get方法获取值 
	/*interface ValueWrapper { //缓存值的Wrapper  
        Object get(); //得到真实的value  
        } */
	public ValueWrapper get(Object key) {
		// TODO Auto-generated method stub
		Object obj= redisMem.getObject(key);
		if(obj != null)
		{
			return new SimpleValueWrapper(obj);
		}
		return null;
	}
	//根据key，和value的类型直接获取value  
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> ty) {
		// TODO Auto-generated method stub
		Object r= get(key).get();
		if (r != null && ty != null && ty.isInstance(r))
		{
			throw new RedisReadException("Cached value is not of required type ["
											+ty.getName()+"]:"+r.getClass().getName());
		}
		return (T)r;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object keyf, Callable<T> arg1) {
		// TODO Auto-generated method stub
		String key = keyf.toString();
		if (redisMem.get().exists(key))
		{
			return (T)this.get(key).get();
		}
		Object r = null;
		try {
			r = arg1.call();
		} catch (Exception e) {
			throw new RedisReadException("Callable thread read error"+e.getMessage());
		}
		this.put(keyf, r);
		return (T)r;
	}

	//缓存的名字
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	//得到底层使用的缓存，如Ehcache 
	public Object getNativeCache() {
		// TODO Auto-generated method stub
		return redisMem;
	}
	//往缓存放数据
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		try {
			redisMem.set(key, value , expire);
		} catch (Exception e) {
			throw new RedisWriteException(key.toString()+","+e.getMessage());
		}
	}

	/**
	 * 	如果值不存在，则添加，用来替代如下代码
	 * Object existingValue = cache.get(key);
	 * if (existingValue == null) {
	 *     cache.put(key, value);
	 *     return null;
	 * } else {
	 *     return existingValue;
	 * }
	 */
	public ValueWrapper putIfAbsent(Object key, Object value) {
		ValueWrapper r = get(key);
		if (r == null)
		{
			this.put(key, value);
			return null;
		}else{
			return r;
		}
	}
	
	private int expire = 0 ; //设置过期时间单位秒

	public int getExpire() {
		return expire;
	}
	public void setExpire(int expire) {
		this.expire = expire;
	}
	

}
