package com.zcs.yunjia.jedis;


import java.io.IOException;
import java.util.Map;

public interface JedisClient {
	public String set(String key, String value);
	public String get(String key);
	public Long hset(String key, String field, String value);
	public String hget(String key, String field);
	public void close() throws IOException;
	public Long expire(String key, int seconds);
	public boolean exists(String key);
	public Long del(String... keys);
	public Long hdel(String key, String... fields);
	public Map<String,String> hGetAll(String key);
	public Long incr(String key);
}
