package use.cache.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import use.cache.com.RedisCacheParameter;
import use.cache.com.RedisCacheUtil;
import use.cache.com.except.RedisReadException;
import use.common.security.BaseInfo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisShareMemory 
{
	protected final Logger log = LoggerFactory.getLogger(RedisShareMemory.class);
	
	private JedisPool fPool = null;

	public JedisPool getPool()
	{
		return fPool;
	}
	public RedisShareMemory(RedisCacheParameter val)
	{
		if (BaseInfo.getSecurityInfo().getInfo() instanceof Boolean)
		{
			this.initialClass(val);
		}
		
	}
	/**
	 * 释放对象到连接池
	 * @param val
	 */
	public void free(Object val)
	{
		try
		{
			if (val == null)
			{
				return ;
			}
			if (!(val instanceof Jedis) )
			{
				return ;
			}
			Jedis b = (Jedis)val;
			b.close();
			//fPool.returnResource(b);
		} 
		catch (Exception e)
		{
			log.error("释放redis对象异常，异常原因："+e.getMessage());
		} 	
	}
	/**
	 * 获取JEDIS对象
	 * @return
	 */
	public Jedis get()
	{
		if (fPool == null)
		{
			return null;
		}
		Jedis r = fPool.getResource();
		r.select(this.dataBaseIndex);
		return r;
	}
	/**
	 * 获取String对象
	 * @param key
	 * @return
	 */
	public String getString(String key)
	{
		Jedis fRedis = null;
		try{
			fRedis = get();
			return fRedis.get(key);
		}catch(Exception e)
		{
			log.error("获取redis字符串对象异常，异常原因："+e.getMessage()+"。key:"+key);
			return null;
		}
		finally{
			free(fRedis);
		}
	}
	/**
	 * 获取Object对象通过String key
	 * @param key
	 * @return
	 */
	public Object getObject(String key)
	{
		Jedis fRedis = null;
		try{
			fRedis = get();
			byte[] b = fRedis.get(key.getBytes());
			return RedisObjectSerial.unserialize(b);
		}catch(Exception e)
		{
			return null;
		}
		finally{
			free(fRedis);
		}
	}
	/**
	 * 获取Object对象通过Object key
	 * @param key
	 * @return
	 */
	public Object getObject(Object key)
	{
		Jedis fRedis = null;
		try{
			if(key instanceof String)
			{
				return getObject((String)key);
			}
			fRedis = get();
			byte[] b = fRedis.get(RedisObjectSerial.serialize(key));
			return RedisObjectSerial.unserialize(b);
		}catch(Exception e)
		{
			return null;
		}
		finally{
			free(fRedis);
		}
	}
	/**
	 * 加入缓存String  
	 * @param key
	 * @param val
	 * @throws Exception
	 */
	public void setValue(String key , String val) throws Exception
	{
		set(key , val , 0);
	}
	/**
	 * 加入缓存String key
	 * @param key
	 * @param obj
	 * @throws Exception
	 */
	public void setValue(String key , Object obj) throws Exception
	{
		set(key , obj , 0);
	}
	/**
	 * 加入值Object key
	 * @param key
	 * @param obj
	 * @throws Exception
	 */
	public void setValue(Object key , Object obj) throws Exception
	{
		set(key , obj , 0);
	}
	
	protected Object initialClass(RedisCacheParameter val)
	{
		/*if (!(BaseInfo.getSecurityInfo().getInfo() instanceof Boolean))
		{
			return null;
		}*/	
		if (val == null)
		{
			return null;
		}
		if (fPool != null)
		{
			return fPool ;
		}
		JedisPoolConfig cnf = new JedisPoolConfig();
		//最大连接数, 默认8个
		cnf.setMaxTotal(val.getMaxTotal());
		//最小空闲连接数, 默认0
		cnf.setMinIdle(val.getMinIdle());
		//最大空闲连接数, 默认8个
		cnf.setMaxIdle(val.getMaxIdle());
		//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
		cnf.setMaxWaitMillis(val.getMaxWaitMillis());
		//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		cnf.setMinEvictableIdleTimeMillis(val.getEvictableIdleTimeMillis());
		//在获取连接的时候检查有效性, 默认false 
		cnf.setTestOnBorrow(val.isTestOnBorrow());
		//在空闲时检查有效性, 默认false
		cnf.setTestWhileIdle(val.isTestWhileIdle());
		//返回给连接池是否验证有效 , 默认false
		cnf.setTestOnReturn(val.isTestOnReturn());
		if(val.getPassword().equals(""))
		{
			fPool = new JedisPool(cnf, val.getIp(),val.getPort());
		}else 
		{
			fPool = new JedisPool(cnf, val.getIp(),val.getPort() , 10000, val.getPassword());
		}
	    return fPool;
	}
	
	public Long getSize()
	{
		Jedis jedis = null;
		try
		{
			jedis = RedisCacheUtil.getMem().get();
			return jedis.dbSize();
		}
		finally
		{
			free(jedis);
		}
	}

	public Set keys(String pattern)
	{
		Set<byte[]> keys = null;
		Jedis jedis = get();
		try{
			keys = jedis.keys(pattern.getBytes());
		}finally{
			free(jedis);
		}
		return keys;
	}
	
	public Collection values(String pattern)
	{
		try {
            Set<byte[]> keys = keys(pattern + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List values = new ArrayList(keys.size());
                for (byte[] key : keys) {
					Object value = getObject(new String(key));
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}
	
	public String set(String key , String value , int expire )
	{
		Jedis fRedis = null;
		try{
			fRedis = get();
			fRedis.set(key, value);
			if(expire != 0){
				fRedis.expire(key, expire);//单位秒
		 	}
			return value;
		}
		finally{
			free(fRedis);
		}
	}
	public Object set(Object key,Object value,int expire) throws Exception
	{
		Jedis fRedis = null;
		try
		{
			byte[] aKey = null;
			if(key instanceof String)
			{
				aKey = ((String)key).getBytes();
			}else {
				aKey = RedisObjectSerial.serialize(key);
			}
			fRedis = get();
			byte[] b = RedisObjectSerial.serialize(value);
			fRedis.set(aKey, b);
			if(expire != 0){
				fRedis.expire(aKey, expire);//单位秒
		 	}
			return value;
		}
		finally{
			free(fRedis);
		}
	}
	
	public void del(Object key) {
		// TODO Auto-generated method stub
		byte[] aKey = null;
		if(key instanceof String)
		{
			aKey =((String)key).getBytes();
		}else {
			try {
				aKey = RedisObjectSerial.serialize(key);
			} catch (IOException e) {
				throw new RedisReadException(e.getMessage());
			}
		}
		Jedis r = null;
		try
		{
			r = get();
			r.del(aKey);
		}finally
		{
			free(r);
		}
	}
	
	public void clear() {
		// TODO Auto-generated method stub
		Jedis r = null;
		try
		{
			r = get();
			r.flushDB();
		}
		finally
		{
			free(r);
		}	
	}
	
	public boolean ExistKey(String key)
	{
		Jedis r = null;
		try
		{
			r = get();
			return r.exists(key);
		}
		finally
		{
			free(r);
		}	
	}
	public String getStringNoExistReturnNull(String key)
	{
		Jedis r = null;
		try
		{
			r = get();
			if(!r.exists(key))
			{
				return null;
			}
			return r.get(key);
		}
		finally
		{
			free(r);
		}
	}
	public Object getObjectNoExistReturnNull(String key)
	{
		Jedis r = null;
		try
		{
			r = get();
			if(!r.exists(key))
			{
				return null;
			}
			byte[] b = r.get(RedisObjectSerial.serialize(key));
			return RedisObjectSerial.unserialize(b);
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			free(r);
		}
	}

	private int dataBaseIndex = 0;

	public int getDataBaseIndex() {
		return dataBaseIndex;
	}
	public RedisShareMemory setDataBaseIndex(int dataBaseIndex) {
		this.dataBaseIndex = dataBaseIndex;
		return this;
	}
	
}
