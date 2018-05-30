package use.cache.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import use.common.util.propertyFile.ReadPropertyValue;


public class RedisCacheParameter 
{
	protected final Logger log = LoggerFactory.getLogger(RedisCacheParameter.class);
	
	private boolean redisEanble = false;
	
	public boolean isRedisEanble() {
		return redisEanble;
	}
	public void setRedisEanble(boolean redisEanble) {
		this.redisEanble = redisEanble;
	}



	private String ip;
	private int port;
	//最大连接数, 默认8个
	private int maxTotal=10; 
	//最小空闲连接数, 默认0
	private int minIdle=0;
	//最大空闲连接数, 默认8个
	private int maxIdle=10;
	//获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
	private int maxWaitMillis=3000;
	//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
	private int evictableIdleTimeMillis=1800000;
	//在获取连接的时候检查有效性, 默认false 
	private boolean testOnBorrow=true;
	//在空闲时检查有效性, 默认false
	private boolean testWhileIdle=false;
	//返回给连接池是否验证有效 , 默认false
	private boolean testOnReturn=false;
	
	//shiro session key shiro存放用户token的ID的key前段固定字符串
	private String shiroPrefix = "";
	//#shiro 使用的redis数据索引，可选0，1，2
	private int shiroDataBaseIndex = 0;
	
	private String password = "";
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}
	public int getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public int getEvictableIdleTimeMillis() {
		return evictableIdleTimeMillis;
	}
	public void setEvictableIdleTimeMillis(int evictableIdleTimeMillis) {
		this.evictableIdleTimeMillis = evictableIdleTimeMillis;
	}
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	public boolean isTestOnReturn() {
		return testOnReturn;
	}
	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
	public String getShiroPrefix() {
		return shiroPrefix;
	}
	public void setShiroPrefix(String shiroPrefix) {
		this.shiroPrefix = shiroPrefix;
	}
	public int getShiroDataBaseIndex() {
		return shiroDataBaseIndex;
	}
	public void setShiroDataBaseIndex(int shiroDataBaseIndex) {
		this.shiroDataBaseIndex = shiroDataBaseIndex;
	}
	public void readParameter(String file)
	{
		log.info("redis 初始化配置文件--start");
		URL uPath = this.getClass().getResource("/");
		File f = new File(uPath.getFile() , file);
		if (!f.exists())
		{
			log.error("redis 读取配置文件不存在，停止初始化");
		}

		InputStream in = null;
		Properties prop = new Properties();
		try
		{
			in = new FileInputStream(f);
	    	prop.load(in);
	    	this.redisEanble = ReadPropertyValue.getBoolean(prop, "mem.enable", true);
	    	this.ip = ReadPropertyValue.getStr(prop, "mem.ip", "127.0.0.1");
	    	this.port = ReadPropertyValue.getInt(prop, "mem.port", 6379);
	    	this.maxTotal = ReadPropertyValue.getInt(prop, "mem.MaxTotal", 8);
	    	this.minIdle=ReadPropertyValue.getInt(prop, "mem.MinIdle", 8);
	    	this.maxIdle=ReadPropertyValue.getInt(prop, "mem.MaxIdle", 8);
	    	this.maxWaitMillis=ReadPropertyValue.getInt(prop, "mem.MaxWaitMillis", 3000);
	    	this.evictableIdleTimeMillis=ReadPropertyValue.getInt(prop, "mem.EvictableIdleTimeMillis", 1800000);
	    	this.testOnBorrow=ReadPropertyValue.getBoolean(prop, "mem.TestOnBorrow", true);
	    	this.testWhileIdle=ReadPropertyValue.getBoolean(prop, "mem.TestWhileIdle", false);
	    	this.testOnReturn=ReadPropertyValue.getBoolean(prop, "mem.TestOnReturn", false);
	    	this.password = ReadPropertyValue.getStr(prop, "mem.password", "");
	    	
	    	this.shiroDataBaseIndex = ReadPropertyValue.getInt(prop, "shiro.database", 0);
	    	this.shiroPrefix = ReadPropertyValue.getStr(prop, "shiro.prefix", "shiro_redis_session:");
	    	log.info("redis 初始化配置文件--end");
	    		
		}catch(Exception er)
		{
			log.error("redis initial property File error , result:"+er.getMessage());
		}
		finally
		{
			try 
			{
				if (in != null)
				{
					in.close();
				}
			} 
			catch (Exception e) 
			{
				log.error("redis destroy property File error , result:"+e.getMessage());
			}
		}
	}
}
