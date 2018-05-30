package use.cache.shiroCache.memStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.CacheException;

@SuppressWarnings({"rawtypes","unchecked"})
public class MemoryStorageMap implements IMemoryStorage{

	private ConcurrentMap mem = null;
	
	public Object getSpringCache() {
		// TODO Auto-generated method stub
		return mem;
	}

	public void setSpringCache(Object cache) {
		// TODO Auto-generated method stub
		if(cache instanceof ConcurrentMap)
		{
			mem =(ConcurrentMap)cache;
		}
	}

	public Object get(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return mem.get(key);
	}

	public Object put(Object key, Object value) throws CacheException {
		// TODO Auto-generated method stub
		mem.put(key, value);
		return value; 
	}

	public Object remove(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return mem.remove(key);
	}

	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		mem.clear();
	}

	public int size() {
		// TODO Auto-generated method stub
		return mem.size();
	}

	
	public Set keys(String key) {
		// TODO Auto-generated method stub
		Set r = new HashSet();
		Iterator a = mem.keySet().iterator();
		while( a.hasNext())
		{
			String entry =  (String)a.next(); 
			if(entry.startsWith(key))
			{
				r.add(key);
			}
		}
		return r;
	}

	public Collection values(String key) {
		// TODO Auto-generated method stub
		Set keys = keys(key);
		List values = new ArrayList();
        for (Object akey : keys) {
				Object value = get(akey);
            if (value != null) {
                values.add(value);
            }
        }
        return Collections.unmodifiableList(values);
	}

	public Object set(Object key, Object value, int expire) throws Exception {
		// TODO Auto-generated method stub
		return put(key ,value);
		
	}

}
