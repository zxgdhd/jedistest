package com.cssiot.junit;

import java.io.IOException;

import org.junit.Test;

import com.cssiot.redis.JedisTool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

public class JedisToolTest {

	/**
	 * 
	 * getJedisPool
	 * void
	 * woow
	 * TODO		测试无参redis连接
	 */
	@Test
	public void getJedisPool() {
		JedisPool pool = JedisTool.getJedisPool();
		if (pool != null) {
			System.out.println("获取JedisPool成功！");
		}
	}

	/**
	 * 
	 * getJedisPoolByConfig
	 * void
	 * woow
	 * TODO		指定JedisPoolConfig、主机地址、端口号、超时时间、连接密码 获取jedispool
	 */
	@Test
	public void getJedisPoolByConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(10000);
		config.setMaxTotal(2000);
		config.setMaxWaitMillis(10000);
		config.setTestOnBorrow(true);
		JedisPool pool = JedisTool.getJedisPool(config, "127.0.0.1", 6379, 10000, null);
		if (pool != null) {
			System.out.println("获取JedisPool成功！");
		}
	}
	
	
	/**
	 * 
	 * jedisPipeLine: starts a pipeline,which is a very efficient way to send
	 * lots of command and read all the responses when you finish sending them
	 * 适合进行批量处理请求
	 * 
	 * void woow 
	 * 
	 * TODO 测试jedis pipeline
	 */
	@Test
	public  void jedisPipeLine(){
		JedisPool pool=JedisTool.getJedisPool();
		Jedis jedis=pool.getResource();
		Pipeline pipeline=jedis.pipelined();
		pipeline.set("name", "oh I'm woow,my name is zxgdhd!");
		Response<String> name=pipeline.get("name");
		
		/*
		 * exec是在事务下使用的，执行之前需要开启使用multi开始事务，之后使用exec执行事务。所以这里没有使用multi开始事务的情况下，
		 * 其实是不应该用exec的，否则会出现ERR EXEC without MULTI的返回
		 */
		//		pipeline.exec();
		pipeline.sync();
		try {
			pipeline.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("The name is "+name.get());
	}
	
	
	
	/**
	 * 
	 * jedisPipeLine: starts a pipeline,which is a very efficient way to send
	 * lots of command and read all the responses when you finish sending them
	 * 适合进行批量处理请求
	 * 
	 * void woow 
	 * 
	 * TODO 测试jedis pipeline exec()方法
	 */
	@Test
	public  void jedisPipeLineSync(){
		JedisPool pool=JedisTool.getJedisPool();
		Jedis jedis=pool.getResource();
		Pipeline pipeline=jedis.pipelined();
		pipeline.set("name", "oh I'm woow,my name is zxgdhd!");
		pipeline.append("name", "I'm coder!");
		pipeline.append("name", "I'm working in CSSIOT!");
		Response<String> name=pipeline.get("name");
		
		/*
		 * exec是在事务下使用的，执行之前需要开启使用multi开始事务，之后使用exec执行事务。所以这里没有使用multi开始事务的情况下，
		 * 其实是不应该用exec的
		 */
		pipeline.multi();
		pipeline.exec();
		try {
			pipeline.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("The name is "+name.get());
	}
	
	/**
	 * 
	 * testJedisTransaction
	 * void
	 * woow
	 * TODO		测试Jedis Transaction
	 */
	@Test
	public void testJedisTransaction(){
		Jedis jedis=JedisTool.getJedis();
		Transaction tx=jedis.multi();
		for (int i = 0; i < 10; i++) {
			tx.append("name", ","+i);
		}
		Response<String> name=tx.get("name");
		tx.exec();
		System.out.println(name.get());
	}

}
