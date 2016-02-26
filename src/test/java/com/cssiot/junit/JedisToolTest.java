package com.cssiot.junit;

import org.junit.Test;

import com.cssiot.redis.JedisTool;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisToolTest {
	
	
	@Test
	public void getJedisPool(){
		JedisPool pool=JedisTool.getJedisPool();
		if (pool!=null) {
			System.out.println("获取JedisPool成功！");
		}
	}
	
	@Test
	public void getJedisPoolByConfig(){
		JedisPoolConfig config=new JedisPoolConfig();
		config.setMaxIdle(10000);
		config.setMaxTotal(2000);
		config.setMaxWaitMillis(10000);
		config.setTestOnBorrow(true);
		JedisPool pool=JedisTool.getJedisPool(config,"127.0.0.1",6379,10000,null);
		if (pool!=null) {
			System.out.println("获取JedisPool成功！");
		}
	}

}
