package uap.web.httpsession.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springside.modules.nosql.redis.JedisTemplate;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import uap.web.utils.PropertyUtil;

/**
 * Session属性管理类，session中需要存储的属性都放到redis缓存中，session丢失后从redis中复制回去
 * <br>
 * 要求session中不能存储复杂的数据结构，且存放的属性建议基本类型,属性值需要实现序列化接口
 * 
 * @author liujmc
 */
public class SessionCacheManager {

	private static SessionCacheManager cacheManager = null; 
	
	private static int sessionTimeout = 3600;
	
	public static JedisTemplate jedisTemplate;
	
	public static SessionCacheManager getInstance() {
		if(cacheManager==null) {
			synchronized (SessionCacheManager.class) {
				if(cacheManager==null) {
					cacheManager = new SessionCacheManager();
					String st = PropertyUtil.getPropertyByKey("sessionTimeout");
					if(st!=null)
						sessionTimeout = Integer.valueOf(st);
				}
			}
		}
		return cacheManager;
	}
	
	/**
	 *初始化
	 */
	public void initSessionCache(String sid, Map<String,Object> map) {
		/*
		String seesionMapStr = SerializUtil.getStrFromObj(map);
		jedisTemplate.set(sid, seesionMapStr);
		// 设置过期时间，应该同web.xml中的sessionTimeout保持一致
		jedisTemplate.getJedisPool().getResource().expire(sid, sessionTimeout);
		*/
		byte[] sidKey = sid.getBytes();
		Jedis jedis = jedisTemplate.getJedisPool().getResource();
		Pipeline p = jedis.pipelined();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object attrObj = map.get(key);
			byte[] attrBytes = null;
			if (attrObj instanceof String) {
				attrBytes = ((String) attrObj).getBytes();
			} else {
				attrBytes = SerializUtil.ObjectToByte(attrObj);
			}
			p.hset(sidKey, key.getBytes(), attrBytes);
		}
		// 设置过期时间，应该同web.xml中的sessionTimeout保持一致
		p.expire(sid, sessionTimeout);
		p.sync();
		jedisTemplate.getJedisPool().returnResource(jedis);
	}

	/*
	@SuppressWarnings("unchecked")
	public Map<String,Object> getSessionCache(String sid) {
		Jedis jedis = jedisTemplate.getJedisPool().getResource();
		byte[] bytes = jedis.get(sid.getBytes());
		if (bytes == null) {
			jedisTemplate.getJedisPool().returnResource(jedis);
			return null;
		} else {
			jedis.expire(sid, sessionTimeout);
			jedisTemplate.getJedisPool().returnResource(jedis);
			return (Map<String, Object>) SerializUtil.ByteToObject(bytes);
		}
	}
	*/
	
	public Map<String,Object> getAllSessionAttrCache(String sid) {
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		Jedis jedis = jedisTemplate.getJedisPool().getResource();
		Map<byte[], byte[]> redisMap = jedis.hgetAll(sid.getBytes());
		
		for (Iterator<byte[]> iterator = redisMap.keySet().iterator(); iterator.hasNext();) {
			byte[] byteKey = iterator.next();
			String key = new String(byteKey);
			redisMap.get(byteKey);
			Object obj = SerializUtil.ByteToObject(redisMap.get(byteKey));
			hashMap.put(key, obj);
		}
		jedis.expire(sid, sessionTimeout);
		jedisTemplate.getJedisPool().returnResource(jedis);
		return hashMap;
	}
	
	public void removeSessionCache(String sid) {
		jedisTemplate.del(sid);
	}
	
	public void putSessionCacheAttribute(String sid, String key, Object value) {
		Jedis jedis = jedisTemplate.getJedisPool().getResource();
		byte[] valueBytes = SerializUtil.ObjectToByte(value);
		jedis.hset(sid.getBytes(), key.getBytes(), valueBytes);
		jedis.expire(sid, sessionTimeout);
		jedisTemplate.getJedisPool().returnResource(jedis);
	}
	
	public Object getSessionCacheAttribute(String sid, String key) {
		Jedis jedis = jedisTemplate.getJedisPool().getResource();
		boolean isExist = jedis.exists(sid);
		Object result = null;
		if(isExist){
			jedis.expire(sid, sessionTimeout);
			byte[] attrBytes = jedis.hget(sid.getBytes(), key.getBytes());
			result = SerializUtil.ByteToObject(attrBytes);
		} 
		jedisTemplate.getJedisPool().returnResource(jedis);
		return result;
	}
	
	public void removeSessionCacheAttribute(String sid, String key) {
		jedisTemplate.hdel(sid, new String[]{key});
	}
}
