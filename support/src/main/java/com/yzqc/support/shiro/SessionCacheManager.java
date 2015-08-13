package com.yzqc.support.shiro;

import com.yzqc.support.shiro.SerializUtil;
import org.apache.commons.lang3.StringUtils;
import org.springside.modules.nosql.redis.JedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Session属性管理类，session中需要存储的属性都放到redis缓存中，session丢失后从redis中复制回去
 * <br>
 * 要求session中不能存储复杂的数据结构，且存放的属性建议基本类型,属性值需要实现序列化接口
 *
 * @author liujmc
 */
public class SessionCacheManager {

    private int sessionTimeout = 3600;

    private JedisTemplate jedisTemplate;

    /**
     * 初始化
     */
    public void initSessionCache(String sid, Map<String, Object> map) {
        /*
        String seesionMapStr = SerializUtil.getStrFromObj(map);
		jedisTemplate.set(sid, seesionMapStr);
		// 设置过期时间，应该同web.xml中的sessionTimeout保持一致
		jedisTemplate.getJedisPool().getResource().expire(sid, sessionTimeout);
		*/
        byte[] sidKey = sid.getBytes(Charset.forName("UTF-8"));
        Jedis jedis = jedisTemplate.getJedisPool().getResource();
        Pipeline p = jedis.pipelined();
        for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Object attrObj = map.get(key);
            byte[] attrBytes = null;
            if (attrObj instanceof String) {
                attrBytes = ((String) attrObj).getBytes(Charset.forName("UTF-8"));
            } else {
                attrBytes = SerializUtil.objectToByte(attrObj);
            }
            p.hset(sidKey, key.getBytes(Charset.forName("UTF-8")), attrBytes);
        }
        // 设置过期时间，应该同web.xml中的sessionTimeout保持一致
        p.expire(sid, sessionTimeout);
        p.sync();
        jedisTemplate.getJedisPool().returnResource(jedis);
    }

    public Map<String, Object> getAllSessionAttrCache(String sid) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        Jedis jedis = jedisTemplate.getJedisPool().getResource();
        Map<byte[], byte[]> redisMap = jedis.hgetAll(sid.getBytes(Charset.forName("UTF-8")));

        for (Iterator<byte[]> iterator = redisMap.keySet().iterator(); iterator.hasNext(); ) {
            byte[] byteKey = iterator.next();
            String key = new String(byteKey, Charset.forName("UTF-8"));
            Object obj = SerializUtil.byteToObject(redisMap.get(byteKey));
            hashMap.put(key, obj);
        }
        jedis.expire(sid, sessionTimeout);
        jedisTemplate.getJedisPool().returnResource(jedis);
        return hashMap;
    }

    public void removeSessionCache(String sid) {
        jedisTemplate.del(sid);
    }

    public <T extends Serializable> void putSessionCacheAttribute(String sid, String key, T value) {
        Jedis jedis = jedisTemplate.getJedisPool().getResource();
        byte[] valueBytes = SerializUtil.objectToByte(value);
        jedis.hset(sid.getBytes(Charset.forName("UTF-8")), key.getBytes(Charset.forName("UTF-8")), valueBytes);
        jedis.expire(sid, sessionTimeout);
        jedisTemplate.getJedisPool().returnResource(jedis);
    }

    public <T extends Serializable> void putSessionCacheAttribute(String key, T value) {
        Jedis jedis = jedisTemplate.getJedisPool().getResource();
        byte[] valueBytes = SerializUtil.objectToByte(value);
        byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
        jedis.set(keyBytes, valueBytes);
        jedis.expire(keyBytes, sessionTimeout);
        jedisTemplate.getJedisPool().returnResource(jedis);
    }

    public <T extends Serializable> void updateSessionCacheAttribute(String sid, String key, T value) {
        Jedis jedis = jedisTemplate.getJedisPool().getResource();
        if (jedis.hexists(sid.getBytes(Charset.forName("UTF-8")), key.getBytes(Charset.forName("UTF-8")))) {
            putSessionCacheAttribute(sid, key, value);
        }
        jedisTemplate.getJedisPool().returnResource(jedis);
    }

    public <T extends Serializable> void updateSessionCacheAttribute(String key, T value) {
        Jedis jedis = jedisTemplate.getJedisPool().getResource();
        if (jedis.exists(key)) {
            putSessionCacheAttribute(key, value);
        }
        jedisTemplate.getJedisPool().returnResource(jedis);
    }

    public <T extends Serializable> T getSessionCacheAttribute(String sid, String key) {
        Jedis jedis = jedisTemplate.getJedisPool().getResource();
        boolean isExist = jedis.exists(sid);
        Object result = null;
        if (isExist) {
            jedis.expire(sid, sessionTimeout);
            byte[] attrBytes = jedis
                    .hget(sid.getBytes(Charset.forName("UTF-8")), key.getBytes(Charset.forName("UTF-8")));
            if (attrBytes == null) {
                return null;
            }
            result = SerializUtil.byteToObject(attrBytes);
        }
        jedisTemplate.getJedisPool().returnResource(jedis);
        return (T) result;
    }

    public <T extends Serializable> T getSessionCacheAttribute(String key) {
        Jedis jedis = jedisTemplate.getJedisPool().getResource();
        boolean isExist = jedis.exists(key);
        Object result = null;
        if (isExist) {
            jedis.expire(key, sessionTimeout);
            byte[] attrBytes = jedis.get(key.getBytes(Charset.forName("UTF-8")));
            if (attrBytes == null) {
                return null;
            }
            result = SerializUtil.byteToObject(attrBytes);
        }
        jedisTemplate.getJedisPool().returnResource(jedis);
        return (T) result;
    }

    public void removeSessionCacheAttribute(String sid, String key) {
        if (StringUtils.isNotBlank(sid) && StringUtils.isNotBlank(key)) {
            jedisTemplate.hdel(sid, key);
        }
    }

    public void removeSessionCacheAttribute(String key) {
        if (StringUtils.isNotBlank(key)) {
            jedisTemplate.del(key);
        }
    }

    public <T extends Serializable> T getCurUser(String uname) {
        return StringUtils.isNotBlank(uname) ? (T) getSessionCacheAttribute(createUserCacheKey(uname)) : null;
    }

    public <T extends Serializable> T getCurUser(HttpServletRequest request) {
        String uname = CookieUtil.findCookieValue(request.getCookies(), CookieConstants.USERNAME);
        if (StringUtils.isNotBlank(uname)) {
            return getCurUser(uname);
        }
        return null;
    }

    public <T extends Serializable> void cacheUser(String uname, T user) {
        putSessionCacheAttribute(createUserCacheKey(uname), user);
    }

    public <T extends Serializable> void refreshUser(String uname, T user) {
        updateSessionCacheAttribute(createUserCacheKey(uname), user);
    }

    public <T extends Serializable> void disCacheUser(String uname) {
        removeSessionCacheAttribute(createUserCacheKey(uname));
    }

    private String createUserCacheKey(String uname) {
        return new StringBuffer(SessionCacheContants.USER_INFO_LOGIN).append(":").append(uname).toString();
    }

    public JedisTemplate getJedisTemplate() {
        return jedisTemplate;
    }

    public void setJedisTemplate(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

}
