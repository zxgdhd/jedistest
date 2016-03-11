package com.cssiot.junit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import com.cssiot.entity.Company;
import com.cssiot.entity.Customer;
import com.cssiot.redis.JedisTool;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.protobuf.InvalidProtocolBufferException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
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
		JedisPool pool = JedisTool.getJedisPool(config, "127.0.0.1", 6380, 10000, null);
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
		JedisPool pool=JedisTool.getJedisPool(6380);
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
	
	/**
	 * 
	 * testSlaveRedis
	 * void
	 * woow
	 * TODO		复制redis.conf到其他文件夹，修改端口和日志文件位置后，
	 * 使用命令：redis-server <config文件位置>，即可开启第二个redis服务
	 * 这里我配置了6379和6380来模拟主从服务器
	 */
	@Test
	public void testSlaveRedis(){
/*		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(10000);
		config.setMaxTotal(2000);
		config.setMaxWaitMillis(10000);
		config.setTestOnBorrow(true);
		JedisPool poolSlave = JedisTool.getJedisPool(config, "127.0.0.1", 6380, 10000, null);
		JedisPool poolMain = JedisTool.getJedisPool(config, "127.0.0.1", 6379, 10000, null);*/
		Jedis jedis=JedisTool.getJedisPool(6379).getResource();
		if(jedis!=null){
			System.out.println("slave redis creat success!");
		}
	}
	
	@Test
	public void testObject(){
		Customer customer=new Customer();
		Jedis jedis=JedisTool.getJedis();
	}
	
	
	/**
	 * 
	 * testDistribute
	 * void
	 * woow
	 * TODO		开启两个不同端口的Jedis服务器，测试ShardedJedis
	 * 			此处本希望通过ShardJedis存入Redis，在分别通过两个Jedis来取出来，实验证明Jedis取出来null
	 * 			择机再行完善
	 */
	@Test
	public void testDistribute(){
		ArrayList<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("127.0.0.1",6379));
		shards.add(new JedisShardInfo("127.0.0.1",6380));
		ShardedJedis shardedJedis=new ShardedJedis(shards);
		String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.EEE").format(new Date(System.currentTimeMillis()));
		shardedJedis.set("time", time);
		System.out.println(time);
//		Jedis jedis79=JedisTool.getJedisPool(6379).getResource();
//		Jedis jedis80=JedisTool.getJedisPool(6380).getResource();
//		System.out.println("Jedis-6379:"+jedis79.get("time")+"\nJedis-6380:"+jedis80.get("time"));
		System.out.println("sharedJedis-time="+shardedJedis.get("time"));
	}
	
	/**
	 * @throws InvalidProtocolBufferException
	 * testProtoBuf
	 * void
	 * woow
	 * TODO		简单测试redis存储protobuf编码的对象，每一个proto文件对应一个java文件，文件中定义的message在java文件中
	 * 			以静态内部类的形式存在，可重复字段（repeated）对应类为com.google.protobuf.Lazy****ArrayList.EMPTY，
	 * 			在protobuf中Map等第三方提供的类又该如何表示呢？
	 * 
	 * 生成java文件命令：
	 * 		--proto_path：proto文件存放的路径
	 * 		--java_out：生成java文件的目标路径，在proto文件中配置了"option java_package="包名""
	 * 			后设置文件输出路径为java文件根路径，java文件自动生成到对应包下
	 * 		最后是proto文件名（全路径名，相对--proto_path目录找不到proto文件）
	 * 
	 * 
	 * protoc --proto_path=/home/woow/wp/jedistool/src/main/java/com/cssiot/proto/ 
	 * --java_out=/home/woow/wp/jedistool/src/main/java/ 
	 * /home/woow/wp/jedistool/src/main/java/com/cssiot/proto/Company.proto
	 */
	@Test
	public void testProtoBuf() throws InvalidProtocolBufferException{
		Company.innerClass innner=Company.innerClass.newBuilder().setRegistrationNum("440301112071157")
				.setCompanyName("深圳市奇倍翔").setCompanyAddress("深圳市")
				.addCompanyStaff("胡*珍").addCompanyStaff("邹*群").build();
		System.out.println("The innerClass toString is "+innner.toString());
		byte[] innerByte=innner.toByteArray();
		Jedis jedis=JedisTool.getJedis();
		jedis.set("inner".getBytes(),innerByte);
		System.out.println("\nGet form redis innerClass is "
		+Company.innerClass.parseFrom(jedis.get("inner".getBytes())).toString());
	}
	
	
	/**
	 * 
	 * testProtoStuff
	 * void
	 * woow
	 * TODO		小试protostuff，简单将对象序列化反序列化，在protostuff中还有pipe相关方法
	 * 			容后再试，并整理出工具类
	 */
	@Test
	public void testProtoStuff(){
		Customer customer = new Customer(true);
		LinkedBuffer linkedBuffer=LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		Schema schema = RuntimeSchema.createFrom(Customer.class);
		System.out.println("customer:"+customer.toString());
		byte[] customerByte = ProtobufIOUtil.toByteArray(customer, schema, linkedBuffer);
		Jedis jedis=JedisTool.getJedis();
		jedis.set("customer".getBytes(), customerByte);
		byte[] customerByteFromJedis=jedis.get("customer".getBytes());
		Customer cus1=new Customer();
		ProtobufIOUtil.mergeFrom(customerByteFromJedis, cus1, schema);
		System.out.println("customer from jedis:"+cus1);
		
	}


}