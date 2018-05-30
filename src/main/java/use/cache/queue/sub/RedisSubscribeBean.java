package use.cache.queue.sub;

import redis.clients.jedis.JedisPubSub;
import use.common.Eventloop.IEventLoopHandle;
import use.common.exception.SystemException;

@SuppressWarnings("rawtypes")
public class RedisSubscribeBean extends JedisPubSub 
{
	private IEventLoopHandle fClass = null;
	
	public RedisSubscribeBean(IEventLoopHandle obj)
	{
		fClass = obj;
	}
	public RedisSubscribeBean(Class c)
	{
		if (IEventLoopHandle.class.isAssignableFrom(c))
		{
			try {
				fClass = (IEventLoopHandle)c.newInstance();
			} catch (Exception e) {
				throw new SystemException(e.getMessage());
			}
		}
	}
	// 取得订阅的消息后的处理 
	public void onMessage(String s, String s1) 
	{
		if(fClass != null)
		{
			fClass.onMessage(s, s1);
		}
	}
	//取得按表达式的方式订阅的消息后的处理 
	public void onPMessage(String s, String s1, String s2) 
	{
	}
	//初始化订阅时候的处理 
	public void onSubscribe(String s, int i) 
	{
	}
	//取消订阅时候的处理
	public void onUnsubscribe(String s, int i) 
	{
	}
	// 取消按表达式的方式订阅时候的处理
	public void onPUnsubscribe(String s, int i)
	{
	}
	// 初始化按表达式的方式订阅时候的处理
	public void onPSubscribe(String s, int i){}
}
