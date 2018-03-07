package com.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	@Test
	public void testJedis(){
		//创建一个连接Jedis对象，参数host，port
		Jedis jedis = new Jedis("192.168.25.129", 6379);
		//直接使用jedis操作redis，所有jedis 的命令都对应一个方法
		jedis.set("testJedis", "test123");
		String testJedis = jedis.get("testJedis");
		System.out.println(testJedis);
		//关闭连接
		jedis.close();
	}
	
	@Test
	public void testJedisPool(){
		//创建一个连接词对象，两个参数host，port
		JedisPool jedisPool = new JedisPool("192.168.25.129",6379);
		//从连接池中获得一个连接，就是jedis对象
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作redis
		jedis.set("testJedisPool", "pool");
		String testJedisPool = jedis.get("testJedisPool");
		System.out.println(testJedisPool);
		//关闭连接，每次使用完毕后关闭连接。连接池回收资源
		jedis.close();
		//关闭连接池
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster(){
		//创建一个JedisCluster对象，有一个参数nodes是一个set类型，set中包含若干个HostAndPort对象
		Set<HostAndPort> nodes = new HashSet();
		nodes.add(new HostAndPort("192.168.25.129", 7001));
		nodes.add(new HostAndPort("192.168.25.129", 7002));
		nodes.add(new HostAndPort("192.168.25.129", 7003));
		nodes.add(new HostAndPort("192.168.25.129", 7004));
		nodes.add(new HostAndPort("192.168.25.129", 7005));
		nodes.add(new HostAndPort("192.168.25.129", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用JedisCluster操作redis
		jedisCluster.set("cc", "33");
		String test = jedisCluster.get("bb");
		System.out.println(test);
		//关闭JedisCluster对象
		jedisCluster.close();
	}
}
