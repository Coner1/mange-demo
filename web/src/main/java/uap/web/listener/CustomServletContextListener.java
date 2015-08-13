package uap.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springside.modules.nosql.redis.JedisTemplate;

import uap.web.httpsession.cache.SessionCacheManager;

public class CustomServletContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent event) {
	}

	public void contextInitialized(ServletContextEvent event) {
		try {
			WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
			JedisTemplate jedisTemplate = wac.getBean("jedisTemplate", JedisTemplate.class);
			SessionCacheManager.jedisTemplate = jedisTemplate;
			System.out.println("SessionCacheMgr inited ....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}