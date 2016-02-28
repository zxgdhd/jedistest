package com.cssiot.redis;


import redis.clients.jedis.Jedis;
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
	 * @param config
	 *            JedisPoolConfig对象
	 * 
	 * 
	 *            maxActive：控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-
	 *            1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted。
	 * 
	 *            maxIdle：控制一个pool最多有多少个状态为idle(空闲)的jedis实例；
	 * 
	 *            whenExhaustedAction：表示当pool中的jedis实例都被allocated完时，pool要采取的操作；
	 *            默认有三种。 WHEN_EXHAUSTED_FAIL -->
	 *            表示无jedis实例时，直接抛出NoSuchElementException； WHEN_EXHAUSTED_BLOCK
	 *            --> 则表示阻塞住，或者达到maxWait时抛出JedisConnectionException；
	 *            WHEN_EXHAUSTED_GROW --> 则表示新建一个jedis实例，也就说设置的maxActive无用；
	 * 
	 *            maxWait：表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，
	 *            则直接抛出JedisConnectionException；
	 * 
	 *            testOnBorrow：在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，
	 *            则得到的jedis实例均是可用的；
	 * 
	 *            testOnReturn：在return给pool时，是否提前进行validate操作；
	 * 
	 *            testWhileIdle：如果为true，表示有一个idle object evitor线程对idle
	 *            object进行扫描，如果validate失败，此object会被从pool中drop掉；
	 *            这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
	 * 
	 *            timeBetweenEvictionRunsMillis：表示idle object
	 *            evitor两次扫描之间要sleep的毫秒数；
	 * 
	 *            numTestsPerEvictionRun：表示idle object evitor每次扫描的最多的对象数；
	 * 
	 *            minEvictableIdleTimeMillis：表示一个对象至少停留在idle状态的最短时间，然后才能被idle
	 *            object
	 *            evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
	 * 
	 *            softMinEvictableIdleTimeMillis：在minEvictableIdleTimeMillis基础上，
	 *            加入了至少minIdle个对象已经在pool里面了。如果为-1，evicted不会根据idle
	 *            time驱逐任何对象。如果minEvictableIdleTimeMillis>0，则此项设置无意义，
	 *            且只有在timeBetweenEvictionRunsMillis大于0时才有意义；
	 * 
	 *            lifo：borrowObject返回对象时，是采用DEFAULT_LIFO（last in first
	 *            out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；
	 * @param host
	 *            redis服务器地址
	 * @param port
	 *            redis服务器端口
	 * @param timeout
	 *            获取链接的最长时间
	 * @param auth
	 *            连接密码
	 * @return JedisPool对象 getJedisPool JedisPool woow TODO 根据配置对象生成JedisTool对象
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
	
	
	/**
	 * @return		Jedis对象
	 * getJedis
	 * Jedis
	 * woow
	 * TODO		获取Jedis对象
	 */
	public static Jedis getJedis(){
		return getJedisPool().getResource();
	}
	
	
	/**
	 * @return		Jedis对象
	 * getJedis
	 * Jedis
	 * woow
	 * TODO		获取Jedis对象
	 */
	public static Jedis getJedis(JedisPoolConfig config,String host,int port,int timeout,String auth){
		return getJedisPool(config,host,port,timeout,auth).getResource();
	}

}
