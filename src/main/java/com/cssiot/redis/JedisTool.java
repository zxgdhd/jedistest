package com.cssiot.redis;


import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author woow
 *
 */
public class JedisTool {
	
//	private JedisPool pool;
	private static JedisPool pool;
	
	/**
	 * @return	JedisPool对象
	 * getJedisPool
	 * JedisPool
	 * woow
	 * TODO		使用双重同步锁实例化Jedis单例对象
	 */
	public static JedisPool getJedisPool(){
		if(pool==null){
			synchronized(JedisTool.class){
				if(pool==null){
					pool=new JedisPool();
				}
			}
		}
		return pool;
	}
	
	/**
	 * @param config	JedisPoolConfig对象
	 * @param host		redis服务器地址
	 * @param port		redis服务器端口
	 * @param timeout	获取链接的最长时间
	 * @param auth		连接密码
	 * @return			JedisPool对象
	 * getJedisPool
	 * JedisPool
	 * woow
	 * TODO		根据配置对象生成JedisTool对象
	 */
	public static JedisPool getJedisPool(JedisPoolConfig config,String host,int port,int timeout,String auth){
		if(pool==null){
			synchronized(JedisTool.class){
				if(pool==null){
					pool=new JedisPool(config,host,port,timeout,auth);
				}
			}
		}
		return pool;
	}

}
