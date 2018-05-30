package use.cache.shiroCache.memStorage;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.CacheException;

import use.cache.redis.RedisShareMemory;

@SuppressWarnings("rawtypes")
public class MemoryStorageRedis implements IMemoryStorage {

	private RedisShareMemory mem = null;

	public Object getSpringCache() {
		// TODO Auto-generated method stub
		return mem;
	}

	public void setSpringCache(Object cache) {
		if(cache instanceof RedisShareMemory)
		{
			mem = ((RedisShareMemory)cache);
		}
	}

	public Object get(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return mem.getObject(key);
	}

	public Object put(Object key, Object value) throws CacheException {
		// TODO Auto-generated method stub
		try 
		{
			mem.setValue(key, value);
		}catch(Exception er)
		{
			throw new CacheException(er.getMessage());
		}
		return value;
	}

	public Object remove(Object key) throws CacheException {
		// TODO Auto-generated method stub
		Object r = get(key);
		mem.del(key);
		return r;
	}

	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		mem.clear();
	}

	public int size() {
		// TODO Auto-generated method stub
		return mem.getSize().intValue();
	}

	public Set keys(String key) {
		// TODO Auto-generated method stub
		return mem.keys(key);
	}

	public Collection values(String key) {
		// TODO Auto-generated method stub
		return mem.values(key);
	}

	public Object set(Object key, Object value, int expire) throws Exception {
		// TODO Auto-generated method stub
		return mem.set(key, value, expire);
	}

}
